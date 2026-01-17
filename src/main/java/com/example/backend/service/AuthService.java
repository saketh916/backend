package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.backend.security.JwtService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Step 2: Verify OTP and register user
    public String registerUser(String username, String email, String rawPassword, String otp) {

        // 1. Verify OTP
        boolean isOtpValid = otpService.verifyOtp(email, otp);
        if (!isOtpValid) {
            return "Invalid or expired OTP";
        }

        // 2. Load temp user
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "Error: No OTP request found";
        }

        User user = userOpt.get();

        // 3. Hash password
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // 4. Update user fields
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setOtp(null);
        user.setOtpExpiry(null);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return "User registered successfully";
    }


    public String login(String email, String rawPassword) {

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return null;
        }

        return jwtService.createToken(user.getEmail());
    }
}
