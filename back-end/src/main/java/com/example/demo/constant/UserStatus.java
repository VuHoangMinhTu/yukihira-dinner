package com.example.demo.constant;

public enum UserStatus {
    ACTIVE("Kích hoạt"), DISABLED("Vô hiệu hóa");
    private final String description;

    UserStatus(String description) {
        this.description = description;
    }
}
