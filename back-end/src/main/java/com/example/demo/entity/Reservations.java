package com.example.demo.entity;

import com.example.demo.constant.ReservationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Reservations {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @CreationTimestamp
    @Column(name = "booking_time")
    LocalDateTime bookingTime;
    @Enumerated(EnumType.STRING)
    ReservationStatus status;
    @Column(name = "phone_number")
    String phoneNumber;
    @Column(name = "number_guest")
    Integer numberGuest;
    @Column(name = "note")
    String note;
    @ToString.Exclude
    @OneToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "dining_table_id")
    DiningTable diningTable;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    Users users;


}
