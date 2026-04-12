package com.example.demo.service.impl;

import com.example.demo.dto.response.CartItemResponseDTO;
import com.example.demo.dto.response.CartResponseDTO;
import com.example.demo.dto.response.FoodResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.service.CartService;
import com.example.demo.utils.GetCurrentUser;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final OptionRepository optionRepository;
    private final CartItemRepository cartItemRepository;
    @Override
    public CartResponseDTO getCart() {
          try {
              Cart cart = getCartForUser();
              CartResponseDTO cartResponseDTO = new CartResponseDTO();
              cartResponseDTO.setUserId(getCurrentUser().getUserId());
              cartResponseDTO.setItems(cart.getItems().stream().map(item -> {
                  CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
                  cartItemResponseDTO.setId(item.getId());
                  cartItemResponseDTO.setProductId(item.getFood().getId());
                  cartItemResponseDTO.setCategoryId(item.getFood().getCategory().getId());
                  cartItemResponseDTO.setCategoryName(item.getFood().getCategory().getName());
                  cartItemResponseDTO.setQuantity(item.getQuantity());
                  cartItemResponseDTO.setProductName(item.getFood().getName());
                  cartItemResponseDTO.setBasePrice(item.getFood().getPrice());
                  cartItemResponseDTO.setExtraPrice(0.0);
                  for(FoodImage img: item.getFood().getImages()){
                      if(img.isPrimary()){
                          cartItemResponseDTO.setImageUrl(img.getImageUrl());
                          break;
                      }
                  }
                  cartItemResponseDTO.setImageUrl(item.getFood().getImages().iterator().next().getImageUrl());
                  return cartItemResponseDTO;
              }).collect(Collectors.toList()));
              return cartResponseDTO;
          } catch (Exception e) {
              e.printStackTrace();
              return null;
          }

    }

    @Override
    public String addToCart(Long productId, Integer quantity, List<FoodResponse.OptionRequestAndResponse> listOptionRequestAndResponse) {
        Cart cart = getCartForUser();
        Food food = foodRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn"));
        if (food.getQuantity() < quantity) {
            return "Xin lỗi, món này chỉ còn " + food.getQuantity() + " suất.";
        }
        Set<Long> inputOptionSet = (listOptionRequestAndResponse != null) ? new HashSet<>(
                listOptionRequestAndResponse.stream()
                        .map(cio -> cio.getId())
                        .collect(Collectors.toSet())
        ) : new HashSet<>();

        CartItem existingItem = null;
        for (CartItem item : cart.getItems()) {
            if (item.getFood().getId().equals(productId)) {
                Set<Long> currentOptionSet = item.getListOption().stream()
                        .map(cio -> cio.getOption().getId())
                        .collect(Collectors.toSet());

                if (currentOptionSet.equals(inputOptionSet)) {
                    existingItem = item;
                    break;
                }
            }
        }

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setFood(food);
            newItem.setQuantity(quantity);

            if (!inputOptionSet.isEmpty()) {
                List<CartItemOption> options = inputOptionSet.stream().map(optId -> {
                    CartItemOption cio = new CartItemOption();
                    cio.setCartItem(newItem);
                    cio.setOption(optionRepository.findById(optId).get());
                    return cio;
                }).collect(Collectors.toList());
                newItem.setListOption(options);
            }
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);

        return "Thêm sản phẩm thành công";
    }

    @Override
    public String removeItemFromCart(Long cartItemId) {
        Cart cart = getCartForUser();

        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(cartItemId));

        if (!removed) {
            return "Không tìm thấy món hàng để xóa";
        }

        cartRepository.save(cart);
        return "Đã xóa món ăn khỏi giỏ hàng";
    }

    @Override
    public String updateQuantity(Long cartItemId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            return removeItemFromCart(cartItemId);
        }
     

        CartItem item = getCartForUser().getItems().stream().filter(itemId -> itemId.getId().equals(cartItemId)).findFirst().get();
        if(item == null) {
            return "Không tìm thấy món ăn";
        }


        String currentUsername = GetCurrentUser.getCurrentUsername();
        if (!item.getCart().getUsers().getUsername().equals(currentUsername)) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa giỏ hàng này");
        }
        Food food = item.getFood();
        if (quantity > food.getQuantity()) {
            return "Không thể cập nhật. Kho chỉ còn " + food.getQuantity() + " sản phẩm.";
        }
        item.setQuantity(quantity);
        cartItemRepository.save(item);

        return "Cập nhật số lượng thành công";
    }
    private Cart getCartForUser(){

        Users currentUsers = getCurrentUser();
        if (currentUsers == null) {
            return null;
        }
        Cart cart = cartRepository.findByUsers_UserId(currentUsers.getUserId()).get();
        return cart;
    }
    private Users getCurrentUser(){
        String userName = GetCurrentUser.getCurrentUsername();
        if (userName == null) {
            return null;
        }
        return userRepository.findByUsername(userName).get();
    }
}
