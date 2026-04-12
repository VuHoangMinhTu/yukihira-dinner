package com.example.demo.dto.request;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@AllArgsConstructor
@Getter
@Setter
public class CreatePaymentLinkRequestBody {

    private String returnUrl;
    private String cancelUrl;
    private List<FoodOrder> foodOrderList;
    private String address;
    @Getter
    @Setter

    public static class FoodOrder {
        private Long categoryId;
        private String productName;
        private Long productId;
        private String description;
        private double price;
        private int quantity;
        private List<Long> optionIds;
        private String imageUrl;
    }
}