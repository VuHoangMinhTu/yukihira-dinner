package com.example.demo.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class FoodResponse {
    private Long id;
    private String name;
    private List<ImageFood> imageFoodList;
    private Double basePrice;
    private CategoryResponse category;
    private List<GroupOptionResponse> groupOptions;
    private Integer quantity;
    @Getter
    @Setter
    public static class ImageFood {
        private Long id;
        private Boolean isThumbnail;
        private String url;

    }
    @Getter
    @Setter
    public static class GroupOptionResponse {
        private Long id;
        private String name;
        private Boolean isRequired;
        private Integer minGroupOption;
        private Integer maxGroupOption;
        private List<OptionRequestAndResponse> options;
    }
    @Getter
    @Setter
    public static class OptionRequestAndResponse {
        private Long id;
        private String name;
        private Double extraPrice;
        private Integer quantity;
    }
}
