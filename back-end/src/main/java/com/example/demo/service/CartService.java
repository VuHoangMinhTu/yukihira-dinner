package com.example.demo.service;

import com.example.demo.dto.response.CartResponseDTO;
import com.example.demo.dto.response.FoodResponse;

import java.util.List;

public interface CartService {
   CartResponseDTO getCart();
   String addToCart(Long productId, Integer quantity, List<FoodResponse.OptionRequestAndResponse> listOptionRequestAndResponse);
   String removeItemFromCart(Long cartItemId);
   String updateQuantity(Long productId, Integer quantity);
}
