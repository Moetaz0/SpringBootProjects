package com.cc.project.Service;

import com.cc.project.Entity.User;
import com.cc.project.Entity.MedicalHistory;
import com.cc.project.Repository.UserRepository;
import com.cc.project.Repository.MedicalHistoryRepository;
import com.cc.project.Security.JwtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtTokenService;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtUtil jwtTokenService, MedicalHistoryRepository medicalHistoryRepository,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.emailService = emailService;
    }

    public static class AuthResult {
        private final String token;
        private final String role;

        public AuthResult(String token, String role) {
            this.token = token;
            this.role = role;
        }

        public String getToken() {
            return token;
        }

        public String getRole() {
            return role;
        }
    }

    public AuthResult login(String email, String rawPassword, boolean rememberMe) {
        String id = email != null ? email.trim() : "";
        String candidatePassword = rawPassword != null ? rawPassword.trim() : "";

        Optional<User> byEmail = userRepository.findByEmail(id);

        Optional<User> userOpt = byEmail.isPresent() ? byEmail : Optional.empty();
        if (userOpt.isEmpty()) {
            log.debug("Login failed: user not found for identifier='{}'", id);
            throw new RuntimeException("USER_NOT_FOUND");
        }

        User user = userOpt.get();

        // Allow authentication for all roles; downstream logic can redirect per role
        if (user.getRole() == null) {
            log.debug("Login failed: no role assigned for user id={}", user.getId());
            throw new RuntimeException("NO_ROLE");
        }

        boolean matches;
        try {
            matches = passwordEncoder.matches(candidatePassword, user.getPassword());
        } catch (Exception e) {
            // If existing passwords are not encoded yet, fallback to plain compare
            matches = candidatePassword.equals(user.getPassword());
        }

        if (!matches) {
            log.debug("Login failed: password mismatch for user id={}", user.getId());
            throw new RuntimeException("BAD_PASSWORD");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        String role = user.getRole() != null ? user.getRole().name() : null;
        claims.put("role", role);
        String subject = user.getEmail();
        Long userId = user.getId();
        String username = user.getUsername();
        String token = jwtTokenService.generateToken(subject, role, userId, username);
        return new AuthResult(token, role);
    }

    public void changePassword(String email, String currentPassword, String newPassword) {

        Optional<User> byEmail = userRepository.findByEmail(email);
        Optional<User> userOpt = byEmail.isPresent() ? byEmail : Optional.empty();
        if (userOpt.isEmpty())
            throw new RuntimeException("User not found");
        User user = userOpt.get();
        boolean matches;
        try {
            matches = passwordEncoder.matches(currentPassword, user.getPassword());
        } catch (Exception e) {
            matches = currentPassword.equals(user.getPassword());
        }
        if (!matches)
            throw new RuntimeException("Current password is incorrect");
        String encoded;
        try {
            encoded = passwordEncoder.encode(newPassword);
        } catch (Exception e) {
            // fallback: store raw only if encoder misconfigured (not recommended)
            encoded = newPassword;
        }
        user.setPassword(encoded);
        userRepository.save(user);
    }

    public AuthResult register(SignUpData data) {
        if (data.email == null || data.email.isBlank())
            throw new RuntimeException("Email is required");
        if (data.password == null || data.password.isBlank())
            throw new RuntimeException("Password is required");
        if (userRepository.findByEmail(data.email).isPresent())
            throw new RuntimeException("Email already in use");
        String encoded;
        try {
            encoded = passwordEncoder.encode(data.password);
        } catch (Exception e) {
            encoded = data.password;
        }
        User user = new User();
        user.setEmail(data.email);
        // use username as username if provided, else email local part
        String uname = (data.username != null && !data.username.isBlank()) ? data.username.trim()
                : data.email.split("@")[0];
        user.setUsername(uname);
        user.setPhoneNumber(data.phone);
        user.setPassword(encoded);
        user.setRole(User.Role.CLIENT);
        user = userRepository.save(user);

        MedicalHistory history = new MedicalHistory();
        history.setUser(user);
        history.setConditions(data.conditions);
        history.setAllergies(data.allergies);
        history.setMedications(data.medications);
        history.setSmokingStatus(data.smoking);
        history.setAlcoholConsumption(data.alcohol);
        if (data.height != null)
            history.setHeightCm(data.height);
        if (data.weight != null)
            history.setWeightKg(data.weight);
        history.setNotes(data.notes);
        medicalHistoryRepository.save(history);

        Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("uid", user.getId());
        claims.put("role", user.getRole().name());
        String token = jwtTokenService.generateToken(data.email, user.getRole().name(), user.getId(),
                user.getUsername());
        return new AuthResult(token, user.getRole().name());
    }

    // Generate and store a reset code for a user's email
    public void requestPasswordReset(String email) {
        if (email == null || email.isBlank())
            throw new RuntimeException("Email required");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("No account for email"));
        if (user.getRole() != User.Role.CLIENT)
            throw new RuntimeException("Password reset allowed only for CLIENT accounts");
        String code = String.format("%06d", new java.security.SecureRandom().nextInt(1_000_000));
        user.setResetCode(code);
        user.setResetCodeExpiry(java.time.Instant.now().plus(java.time.Duration.ofMinutes(10)));
        userRepository.save(user);
        emailService.sendResetCode(user.getEmail(), code);
    }

    public boolean verifyResetCode(String email, String code) {
        if (email == null || code == null)
            return false;
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null)
            return false;
        if (user.getResetCode() == null || user.getResetCodeExpiry() == null)
            return false;
        if (!code.equals(user.getResetCode()))
            return false;
        if (java.time.Instant.now().isAfter(user.getResetCodeExpiry()))
            return false;
        return true;
    }

    public void resetPassword(String email, String code, String newPassword) {
        if (email == null || code == null || newPassword == null || newPassword.isBlank())
            throw new RuntimeException("Invalid reset data");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("No account for email"));
        if (!verifyResetCode(email, code))
            throw new RuntimeException("Invalid or expired code");
        String np = newPassword.trim();
        String encoded;
        try {
            encoded = passwordEncoder.encode(np);
        } catch (Exception e) {
            encoded = np;
        }
        user.setPassword(encoded);
        user.setResetCode(null);
        user.setResetCodeExpiry(null);
        userRepository.save(user);
    }

    public static class SignUpData {
        public String username;
        public String email;
        public String phone;
        public String password;
        public String conditions;
        public String allergies;
        public String medications;
        public String smoking;
        public String alcohol;
        public Integer height;
        public Double weight;
        public String notes;
    }
}
