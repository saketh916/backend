package com.example.backend.dto;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String username;
    private String email;
    private String password;
    private String otp;
}
