package com.example.demo.repository;

import com.example.demo.entity.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
@Repository
public interface FoodRepository extends JpaRepository<Food, Long>, JpaSpecificationExecutor<Food> {

    @Override
    @EntityGraph(attributePaths = {"images", "category", "groupOptions"})
    List<Food> findAll(Specification<Food> spec);
    Optional<Food> findById(Long id);
}
