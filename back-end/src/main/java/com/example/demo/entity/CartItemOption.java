package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_item_option")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
public class CartItemOption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cart_item_id")
    private CartItem cartItem;
    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;
}

