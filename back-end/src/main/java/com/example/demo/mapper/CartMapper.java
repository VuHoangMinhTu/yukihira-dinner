package com.example.demo.mapper;

import com.example.demo.dto.response.CartItemResponseDTO;
import com.example.demo.dto.response.CartResponseDTO;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.repository.CartItemRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
@Mapper(componentModel = "spring")

public interface CartMapper {
    @Mapping(target = "userId", source = "cart", qualifiedByName = "getUserId")
    @Mapping(target = "items", source = "cart", qualifiedByName = "mapToCartItemResponse")
    CartResponseDTO mapToCartResponseDTO(Cart cart, @Context CartItemRepository cartItemRepository);




    @Named("getUserId")
    default Long getUserId(Cart cart) {
        return cart.getUsers().getUserId();
    }
    @Named("mapToCartItemResponse")
    default List<CartItemResponseDTO> mapToCartItemResponse(Cart cart, @Context CartItemRepository cartItemRepository) {
        List<CartItem> listCartItem = cartItemRepository.getListCartItemByCartId(cart.getId());
        if(listCartItem == null) return Collections.emptyList();
        return listCartItem.stream().map(
                item -> {
                    CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
                    if(item == null){
                       return null;
                    }else {
                        cartItemResponseDTO.setProductId(item.getFood().getId());
                        cartItemResponseDTO.setProductName(item.getFood().getName());
                        cartItemResponseDTO.setCategoryId(item.getFood().getCategory().getId());
                        cartItemResponseDTO.setCategoryName(item.getFood().getCategory().getName());
                        cartItemResponseDTO.setQuantity(item.getQuantity());
                        cartItemResponseDTO.setBasePrice(item.getFood().getPrice());
                    }
                    cartItemResponseDTO.setExtraPrice(0.0);
                    return cartItemResponseDTO;
                }
        ).toList();
    }
}
