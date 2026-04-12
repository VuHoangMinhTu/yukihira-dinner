package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    String description;
    Double price;
    Integer quantity;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "food")
    @ToString.Exclude
    @BatchSize(size = 10)
    Set<FoodImage> images = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;
    @ToString.Exclude
    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    List<GroupOption> groupOptions = new ArrayList<>();
}
