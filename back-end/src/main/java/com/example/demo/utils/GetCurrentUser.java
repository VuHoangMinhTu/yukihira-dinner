package com.example.demo.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

public class GetCurrentUser {
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        // 1. TRƯỜNG HỢP QUAN TRỌNG NHẤT: Khi dùng JWT (Resource Server)
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            // Mặc định Spring lấy "sub" làm subject, chính là "yato" trong token của bạn
            return jwt.getSubject();
        }

        // 2. Dự phòng cho các trường hợp dùng Session hoặc Authentication thông thường
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }

        if (principal instanceof String) {
            return (String) principal;
        }

        return null;
    }
}