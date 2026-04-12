package com.example.demo.specifications;

import com.example.demo.entity.Food;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public  class FoodSpecifications {

    public static Specification<Food> filterByCategory(String category) {
         return new Specification<Food>() {
             @Override
             public @Nullable Predicate toPredicate(Root<Food> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                 if(category == null || category.isEmpty()) return criteriaBuilder.conjunction();
                 return root.get("category").get("name").in(category);
             }
         };
    }
    public static Specification<Food> filterByFoodName(String name) {
        return new Specification<Food>() {
            @Override
            public @Nullable Predicate toPredicate(Root<Food> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(name == null || name.isEmpty()) return criteriaBuilder.conjunction();
                return criteriaBuilder.like(root.get("name"), "%" + name.toLowerCase() + "%");
            }
        };
    }
    public static Specification<Food> filterByPrice(double minPrice, double maxPrice) {
        return new Specification<Food>() {
            @Override
            public @Nullable Predicate toPredicate(Root<Food> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (minPrice == 0 || maxPrice == 0) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            }
        };
    }

}
