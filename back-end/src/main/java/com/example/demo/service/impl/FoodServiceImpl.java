package com.example.demo.service.impl;

import com.example.demo.dto.response.FoodResponse;
import com.example.demo.entity.Food;
import lombok.RequiredArgsConstructor;
import com.example.demo.mapper.FoodMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.demo.repository.FoodRepository;
import com.example.demo.service.FoodService;
import com.example.demo.specifications.FoodSpecifications;
import java.util.List;
@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;
    private final FoodMapper foodMapper;
    @Override
    public Page<FoodResponse> filterFood(String category, String foodName, int page, int size, double minPrice, double maxPrice) {
        Specification<Food> specification = Specification.where(FoodSpecifications.filterByCategory(category)).and(FoodSpecifications.filterByFoodName(foodName)).and(FoodSpecifications.filterByPrice(minPrice, maxPrice));
        Pageable pageable = PageRequest.of(page, size);
        List<Food> foodDB = foodRepository.findAll(specification);
        System.out.println("Có in ra danh sách food ko: "+foodDB.get(0).getImages().iterator().next());
        List<FoodResponse> foodResponse = foodMapper.mapToFoodResponseDTO(foodDB);
        Page<FoodResponse> foodResponsePage = new PageImpl<>(foodResponse, pageable, foodDB.size());
        return foodResponsePage;
    }
}
