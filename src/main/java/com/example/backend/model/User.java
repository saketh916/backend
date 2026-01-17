package com.example.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

/**
 * Represents a user document stored in MongoDB.
 */
@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;

    // Added for OTP verification
    private String otp;
    private LocalDateTime otpExpiry;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
