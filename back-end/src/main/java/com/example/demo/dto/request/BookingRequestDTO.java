package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingRequestDTO {
    private Long diningTableId;
    private String tableName;
    private String nameCustomer;
    private String phoneNumber;
    private String email;
    private LocalDateTime timeArrival;
    private Integer quantityGuest;
    private String note;
}
