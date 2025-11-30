package com.cc.project.Service;

import com.cc.project.Entity.User;
import com.cc.project.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User update(Long id, User user) {
        User existing = getById(id);

        Optional<User> sameUsername = userRepository.findByUsername(user.getUsername());
        if (sameUsername.isPresent() && !sameUsername.get().getId().equals(id)) {
            throw new RuntimeException("Username already taken");
        }

        existing.setUsername(user.getUsername());
        existing.setRole(user.getRole());
        existing.setEmail(user.getEmail());
        existing.setPhoneNumber(user.getPhoneNumber());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(encoder.encode(user.getPassword()));
        }

        return userRepository.save(existing);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
