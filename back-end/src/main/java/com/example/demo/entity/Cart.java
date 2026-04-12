package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "cart")
@NoArgsConstructor
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users users;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart", orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();


}
