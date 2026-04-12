package com.example.demo.service;

public interface OtpRedisService {
    public void saveOtp(String email, String otp);
    public boolean isValid(String email, String inputOtp);
    public void clearOtp(String email);

}
