package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_details")
public class UserDetails {
    @Id
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "fullname")
    private String fullname;
    @Column(name = "dob")
    private LocalDate dob;
    @Column(name = "phone_num")
    private String phoneNum;
    @Column(name = "gender")
    private String gender;
    @Column(name = "img")
    private String img;
    @Column(name = "otp")
    private String otp;
    @Column(name = "otp_expiry_time")
    private LocalDate otpExpiryTime;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private Users users;
}
