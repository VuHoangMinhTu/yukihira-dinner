package com.example.demo.mapper;

import com.example.demo.dto.response.CategoryResponse;
import com.example.demo.dto.response.FoodResponse;
import com.example.demo.entity.Category;
import com.example.demo.entity.Food;
import com.example.demo.entity.FoodImage;
import com.example.demo.entity.GroupOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
@Mapper(componentModel = "spring")
public interface FoodMapper {

    @Mapping(target = "imageFoodList", source = "images", qualifiedByName = "mapImageToImageFoodResponse")
    @Mapping(target = "basePrice", source = "price")
    @Mapping(target = "category", source = "category", qualifiedByName = "mapToCategoryResponse")
    @Mapping(target = "groupOptions", source = "groupOptions", qualifiedByName = "mapToGroupOptionResponse")
    @Mapping(target = "quantity", source = "quantity")
    FoodResponse mapToFoodResponse(Food food);

    List<FoodResponse> mapToFoodResponseDTO(List<Food> foods);



    @Named("mapImageToImageFoodResponse")
    default List<FoodResponse.ImageFood> mapImageToImageFoodResponse(Set<FoodImage> images) {
        if(images == null) {
            return  Collections.emptyList();
        }
        return images.stream().map(image ->{
            FoodResponse.ImageFood imageFood = new FoodResponse.ImageFood();
            imageFood.setId(image.getId());
            imageFood.setUrl(image.getImageUrl());
            imageFood.setIsThumbnail(image.isPrimary());
            return imageFood;
        }).collect(Collectors.toList());
    }
    @Named("mapToCategoryResponse")
    default CategoryResponse mapToCategoryResponse(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        return categoryResponse;
    }
    @Named("mapToGroupOptionResponse")
    default List<FoodResponse.GroupOptionResponse> mapToGroupOptionResponse(List<GroupOption> groupOptions) {
       if (groupOptions == null) return Collections.emptyList();
        return groupOptions.stream().map(groupOption -> {
            FoodResponse.GroupOptionResponse groupOptionResponse = new FoodResponse.GroupOptionResponse();
            groupOptionResponse.setId(groupOption.getId());
            groupOptionResponse.setName(groupOption.getGroupName());
            groupOptionResponse.setIsRequired(groupOption.getIsRequired());
            groupOptionResponse.setMinGroupOption(groupOption.getMinGroupOption());
            groupOptionResponse.setMaxGroupOption(groupOption.getMaxGroupOption());
            groupOptionResponse.setOptions(
                    groupOption.getItems() == null ? Collections.emptyList() :
                            groupOption.getItems().stream().map(
                                    option -> {
                                        FoodResponse.OptionRequestAndResponse optionResponse = new FoodResponse.OptionRequestAndResponse();
                                        optionResponse.setId(option.getId());
                                        optionResponse.setName(option.getOptionName());
                                        optionResponse.setExtraPrice(option.getExtraPrice());
                                        optionResponse.setQuantity(option.getQuantity());
                                        return optionResponse;
                                    }
                            ).collect(Collectors.toList())
            );
            return groupOptionResponse;
        }).collect(Collectors.toList());
    }

}
