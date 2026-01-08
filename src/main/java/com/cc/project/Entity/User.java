package com.cc.project.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String phoneNumber;
    @JsonIgnore
    private String password;

    // Password reset flow fields
    @JsonIgnore
    private String resetCode;
    @JsonIgnore
    private java.time.Instant resetCodeExpiry;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role;

    public enum Role {
        ADMIN, CLIENT, DOCTOR, HOSPITALS, LABS,
    }

}
