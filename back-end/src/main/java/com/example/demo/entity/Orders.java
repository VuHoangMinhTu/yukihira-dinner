package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(name = "order_id_payos")
    String orderIdPayos;
    @Column(name = "total_price")
    Double totalPrice;
    String address;
    String status;
    @Column(name = "order_date")
    LocalDateTime orderDate;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    Users users;
    @ToString.Exclude
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems = new ArrayList<>();

}
