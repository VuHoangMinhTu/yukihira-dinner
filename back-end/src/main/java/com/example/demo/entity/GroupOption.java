package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "group_option")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GroupOption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(name = "group_name")
    String groupName;
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;
    @Column(name = "min_group_option")
    private Integer minGroupOption;
    @Column(name = "max_group_option")
    private Integer maxGroupOption;
    @Column(name = "is_required")
    private Boolean isRequired;
    @OneToMany(mappedBy = "groupOption", cascade = CascadeType.ALL)
    private List<Option> items = new ArrayList<>();
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "cart_item_id")
    private CartItem cartItem;
}
