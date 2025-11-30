package com.cc.project.Config;

import com.cc.project.Entity.User;
import com.cc.project.Repository.UserRepository;
import com.cc.project.Entity.User.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedTestUser(UserRepository users, PasswordEncoder encoder) {
        return args -> {
            String username = "testuser";
            String email = "test@example.com";
            String phone = "0000000000";
            String rawPassword = "Test@1234";

            boolean exists = users.findByUsername(username).isPresent() ||
                    users.findByEmail(email).isPresent() ||
                    users.findByPhoneNumber(phone).isPresent();

            if (!exists) {
                User u = new User();
                u.setUsername(username);
                u.setEmail(email);
                u.setPhoneNumber(phone);
                u.setPassword(encoder.encode(rawPassword));
                try {
                    u.setRole(Role.CLIENT);
                } catch (Throwable ignored) {
                }
                users.save(u);
            }
        };
    }
}
