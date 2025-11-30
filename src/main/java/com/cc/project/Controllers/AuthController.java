package com.cc.project.Controllers;

import com.cc.project.Service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth-login"; // src/main/resources/templates/login.html
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> doLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "rememberMe", defaultValue = "false") boolean rememberMe,
            HttpServletResponse response) {
        try {
            if (email == null || email.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "error", "Missing email",
                        "message", "Provide 'email'"));
            }
            AuthService.AuthResult result = authService.login(email, password, rememberMe);
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + result.getToken());
            String target = roleToDashboardPath(result.getRole());
            return ResponseEntity.ok(Map.of(
                    "token", result.getToken(),
                    "type", "Bearer",
                    "role", result.getRole(),
                    "redirect", target,
                    "rememberMe", rememberMe));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Invalid credentials",
                    "message", ex.getMessage()));
        }
    }

    // Optional JSON-based login for clients posting JSON
    public static class LoginRequest {
        public String email;
        public String password;
        public Boolean rememberMe;
    }

    @PostMapping(value = "/login-json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> doLoginJson(@RequestBody LoginRequest body,
            HttpServletResponse response) {
        String email = body.email;
        String password = body.password;
        boolean rememberMe = body.rememberMe != null ? body.rememberMe : false;
        try {
            if (email == null || email.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "error", "Missing email",
                        "message", "Provide 'email'"));
            }
            AuthService.AuthResult result = authService.login(email, password, rememberMe);
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + result.getToken());
            String target = roleToDashboardPath(result.getRole());
            return ResponseEntity.ok(Map.of(
                    "token", result.getToken(),
                    "type", "Bearer",
                    "role", result.getRole(),
                    "redirect", target,
                    "rememberMe", rememberMe));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Invalid credentials",
                    "message", ex.getMessage()));
        }
    }

    private String roleToDashboardPath(String role) {
        if (role == null)
            return "/dashboard";
        switch (role.toUpperCase()) {
            case "ADMIN":
                return "/dashboard";
            case "CLIENT":
                return "/dashboard/client";
            case "DOCTOR":
                return "/doctor";
            case "HOSPITALS":
                return "/hospital";
            case "LABS":
                return "/lab";
            default:
                return "/dashboard";
        }
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // src/main/resources/templates/signup.html
    }

    // Signup JSON
    public static class SignUpRequest {
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

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> doSignupJson(@RequestBody SignUpRequest body) {
        try {
            AuthService.SignUpData data = new AuthService.SignUpData();
            data.username = body.username;
            data.email = body.email;
            data.phone = body.phone;
            data.password = body.password;
            data.conditions = body.conditions;
            data.allergies = body.allergies;
            data.medications = body.medications;
            data.smoking = body.smoking;
            data.alcohol = body.alcohol;
            data.height = body.height;
            data.weight = body.weight;
            data.notes = body.notes;
            AuthService.AuthResult result = authService.register(data);
            String redirect = "/dashboard/client";
            return ResponseEntity.ok(Map.of(
                    "token", result.getToken(),
                    "type", "Bearer",
                    "role", result.getRole(),
                    "redirect", redirect));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Signup failed",
                    "message", ex.getMessage()));
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @GetMapping("/verify-code")
    public String verifyCodePage() {
        return "verify-code";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "reset-password";
    }

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }

    // Change password endpoint
    public static class ChangePasswordRequest {
        public String currentPassword;
        public String newPassword;
    }

    @PostMapping(value = "/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> changePassword(
            @RequestBody ChangePasswordRequest body,
            @AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Unauthorized",
                    "message", "Sign in to change password"));
        }
        if (body == null || body.currentPassword == null || body.newPassword == null
                || body.currentPassword.isBlank() || body.newPassword.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Invalid request",
                    "message", "Provide currentPassword and newPassword"));
        }
        try {
            authService.changePassword(principal.getUsername(), body.currentPassword, body.newPassword);
            return ResponseEntity.ok(Map.of("status", "ok"));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Unable to change password",
                    "message", ex.getMessage()));
        }
    }

    // Forgot password flow (form posts)
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, org.springframework.ui.Model model) {
        try {
            authService.requestPasswordReset(email);
            model.addAttribute("email", email);
            return "verify-code";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "forgot-password";
        }
    }

    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam("email") String email, @RequestParam("code") String code,
            org.springframework.ui.Model model) {
        boolean ok = authService.verifyResetCode(email, code);
        if (ok) {
            model.addAttribute("email", email);
            model.addAttribute("code", code);
            return "reset-password";
        } else {
            model.addAttribute("email", email);
            model.addAttribute("error", "Invalid or expired code");
            return "verify-code";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String email, @RequestParam("newPassword") String newPassword,
            @RequestParam(value = "code", required = false) String code, org.springframework.ui.Model model) {
        try {
            if (code == null || code.isBlank()) {
                model.addAttribute("email", email);
                model.addAttribute("error", "Missing code");
                return "reset-password";
            }
            authService.resetPassword(email, code, newPassword);
            return "success"; // reuse success page
        } catch (RuntimeException ex) {
            model.addAttribute("email", email);
            model.addAttribute("code", code);
            model.addAttribute("error", ex.getMessage());
            return "reset-password";
        }
    }
}
