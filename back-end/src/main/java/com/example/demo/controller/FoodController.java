package com.example.demo.controller;

import com.example.demo.dto.AppResponse;
import com.example.demo.dto.response.FoodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.FoodService;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;
   @GetMapping
    public AppResponse<Page<FoodResponse>> getFoodWithFilter(@RequestParam(defaultValue ="" ) String category, @RequestParam(defaultValue = "") String foodName, @RequestParam(defaultValue = "0") double minPrice,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "0")  double maxPrice) {
       Page<FoodResponse> result = foodService.filterFood(category, foodName, page, 10, minPrice, maxPrice);
        if(result != null) {
            return AppResponse.<Page<FoodResponse>>builder().data(result).build();
        }else {
            return AppResponse.<Page<FoodResponse>>builder().message("Lỗi khi lấy data").code(999).data(null).build();
        }
   }
}
