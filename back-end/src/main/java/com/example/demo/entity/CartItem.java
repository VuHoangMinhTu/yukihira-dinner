package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@Table(name = "cart_item")
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "food_id")
    private Food food;
    private int quantity;
    @ManyToOne()
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @OneToMany(mappedBy = "cartItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemOption> listOption = new ArrayList<>();

}
