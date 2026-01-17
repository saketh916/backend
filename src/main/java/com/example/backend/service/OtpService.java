package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    // Generate a 6-digit random OTP
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Step 1: Send OTP to user email
    public boolean sendOtp(String email) {

        // Check if user already exists
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            return false; // user already registered
        }

        String otp = generateOtp();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        // Create temporary user object to store OTP
        User tempUser = new User();
        tempUser.setEmail(email);
        tempUser.setOtp(otp);
        tempUser.setOtpExpiry(expiry);

        userRepository.save(tempUser);  // save user with OTP only

        // Send email
        emailService.sendOtpEmail(email, otp);

        return true;
    }

    // Step 2: Verify OTP
    public boolean verifyOtp(String email, String otp) {

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        // Check OTP validity
        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            return false;
        }

        // Check expiry
        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return false;
        }

        return true;
    }
}
