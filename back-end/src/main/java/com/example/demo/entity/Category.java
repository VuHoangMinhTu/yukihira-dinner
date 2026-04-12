package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    @ToString.Exclude
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "category_parent_id")
    Category categoryParent;
    @ToString.Exclude
    @OneToMany(mappedBy = "categoryParent", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Category> categories = new ArrayList<>();
    @ToString.Exclude
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true )
    List<Food> foods = new ArrayList<>();

}
