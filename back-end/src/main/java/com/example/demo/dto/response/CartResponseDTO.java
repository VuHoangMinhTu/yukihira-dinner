package com.example.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponseDTO {

  private Long userId;
  private List<CartItemResponseDTO> items;
}
