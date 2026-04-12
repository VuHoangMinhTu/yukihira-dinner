package com.example.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponseDTO {
    Long id;
    Long productId;
    String productName;
    Long categoryId;
    String categoryName;
    Integer quantity;
    Double basePrice;
    Double extraPrice;
    String imageUrl;
}
