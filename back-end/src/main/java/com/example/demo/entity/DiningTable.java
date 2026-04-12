package com.example.demo.entity;

import com.example.demo.constant.TableStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "dining_table")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class DiningTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(name = "table_name", unique = true)
    String tableName;
    @Column(name = "capacity")
    Integer capacity;
    @Enumerated(EnumType.STRING)
    @Column(name = "table_status")
    TableStatus tableStatus;
    @Column(name = "arrival_time")
    LocalDateTime arrivalTime;
    @Column(name = "expire_time")
    LocalDateTime expireTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_reserved_table_id")
    Users users;
}
