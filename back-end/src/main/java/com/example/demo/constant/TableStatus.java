package com.example.demo.constant;

public enum TableStatus {
    AVAILABLE("Bàn đang trống"),
    RESERVED("Khách đã đặt bàn"),
    OCCUPIED("Khách đang sử dụng bàn")
    ;
    private String status;
     TableStatus(String status) {
        this.status = status;
    }
}
