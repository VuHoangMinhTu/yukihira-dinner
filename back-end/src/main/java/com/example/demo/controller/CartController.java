package com.example.demo.controller;

import com.example.demo.dto.AppResponse;
import com.example.demo.dto.request.CartItemRequestDTO;
import com.example.demo.dto.response.CartResponseDTO;
import lombok.RequiredArgsConstructor;
import com.example.demo.mapper.CartMapper;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.CartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final CartMapper cartMapper;
  @GetMapping("/get-cart")
    public AppResponse<CartResponseDTO> getCartForUser(){
       try{
           CartResponseDTO cartResponseDTO = cartService.getCart();
           if(cartResponseDTO == null){
               return  AppResponse.<CartResponseDTO>builder().code(999).message("Cart not found").data(null).build();
           }
          return AppResponse.<CartResponseDTO>builder().data(cartResponseDTO).build();
       }catch (Exception e){
           e.printStackTrace();
           return  AppResponse.<CartResponseDTO>builder().code(999).message("There is an error").data(null).build();
       }
  }
    @PutMapping("/update-quantity/{cartItemId}/{quantity}")
    public AppResponse<String> updateQuantity(@PathVariable Long cartItemId, @PathVariable Integer quantity){
        try{
            String result = cartService.updateQuantity(cartItemId, quantity);
            return AppResponse.<String>builder().data(result).build();
        }catch (Exception e){
            e.printStackTrace();
            return  AppResponse.<String>builder().code(999).message("There is an error").data(null).build();
        }
    }
    @PostMapping("/add-to-cart")
    public AppResponse<String> addToCart(@RequestBody CartItemRequestDTO cartItemRequestDTO){
        try{
            String result = cartService.addToCart(cartItemRequestDTO.getProductId(), cartItemRequestDTO.getQuantity(), cartItemRequestDTO.getSelectedOptions());
            return AppResponse.<String>builder().data(result).build();
        }
        catch (Exception e){
            e.printStackTrace();
            return  AppResponse.<String>builder().code(999).message("There is an error").data(null).build();
        }
    }
}
