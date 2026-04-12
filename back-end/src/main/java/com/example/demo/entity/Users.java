package com.example.demo.entity;

import com.example.demo.constant.EUserStatus;
import com.example.demo.constant.UserStatus;
import com.example.demo.constant.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "verified")
    private boolean verified;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EUserStatus status;
    @Column(name = "phone_number")
    private String phoneNumber;
    @ToString.Exclude
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiningTable> diningTables = new ArrayList<>();
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Orders> orders = new ArrayList<>();
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Reservations> reservations = new ArrayList<>();
    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    private UserRole roles;
    @Column(name = "created_date")
    private LocalDate created_date;
    @OneToOne(mappedBy = "users", fetch = FetchType.LAZY)
    private UserDetails userDetails;
}
