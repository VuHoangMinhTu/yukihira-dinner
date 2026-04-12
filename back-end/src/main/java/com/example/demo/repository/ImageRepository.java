package com.example.demo.repository;

import com.example.demo.entity.FoodImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<FoodImage, Long> {

}
