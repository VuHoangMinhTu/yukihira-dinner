package com.example.demo.dto.request;

import com.example.demo.dto.response.FoodResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequestDTO {

    private Long productId;
    private Integer quantity;
    private List<FoodResponse.OptionRequestAndResponse> selectedOptions;
}
