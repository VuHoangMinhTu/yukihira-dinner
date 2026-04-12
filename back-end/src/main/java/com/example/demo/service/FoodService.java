package com.example.demo.service;

import com.example.demo.dto.response.FoodResponse;
import org.springframework.data.domain.Page;

public interface FoodService {
    Page<FoodResponse> filterFood(String category, String foodName, int page, int size, double minPrice, double maxPrice);
}
