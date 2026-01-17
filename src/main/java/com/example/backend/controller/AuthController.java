package com.example.backend.controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.SendOtpRequest;
import com.example.backend.dto.VerifyOtpRequest;
import com.example.backend.service.AuthService;
import com.example.backend.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")  // allow frontend
public class AuthController {

    private final OtpService otpService;
    private final AuthService authService;

    // 1️⃣ Send OTP
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody SendOtpRequest request) {

        boolean sent = otpService.sendOtp(request.getEmail());

        if (!sent) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseMessage(false, "User already exists"));
        }

        return ResponseEntity.ok(new ResponseMessage(true, "OTP sent to email"));
    }

    // 2️⃣ Verify OTP & Register User
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {

        String result = authService.registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getOtp()
        );

        boolean success = result.equals("User registered successfully");

        return ResponseEntity.ok(new ResponseMessage(success, result));
    }

    // 3️⃣ Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        String jwt = authService.login(request.getEmail(), request.getPassword());

        if (jwt == null) {
            return ResponseEntity
                    .status(403)
                    .body(new ResponseMessage(false, "Invalid email or password"));
        }

        return ResponseEntity.ok(
                new LoginResponse(true, "Login successful", jwt, request.getEmail())
        );
    }

    // ------------ Internal DTOs for clean JSON response ------------------

    record ResponseMessage(boolean success, String message) {}

    record LoginResponse(boolean success, String message, String jwtToken, String email) {}
}
