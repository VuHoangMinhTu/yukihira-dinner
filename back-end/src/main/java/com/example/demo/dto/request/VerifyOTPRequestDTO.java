package com.example.demo.dto.request;

import lombok.Getter;

@Getter
public class VerifyOTPRequestDTO {
    private String email;
    private String otp;
}
