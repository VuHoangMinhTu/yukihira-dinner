package com.example.demo.service.impl;

import com.example.demo.dto.response.CategoryResponse;
import com.example.demo.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    @Transactional
    public List<CategoryResponse> getAllCategories() {
        List<Category> listCategory = categoryRepository.findAllByCategoryParentIsNull();
       if(listCategory.isEmpty()){
           return null;
       }else {
           return listCategory.stream()
                   .map(this::convertCategoryToCategoryResponse)
                   .collect(Collectors.toList());
       }
    }
    private CategoryResponse convertCategoryToCategoryResponse(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        if(category.getCategories() != null && !category.getCategories().isEmpty()) {
            categoryResponse.setCategoryChildren(
                    category.getCategories().stream()
                            .map(this::convertCategoryToCategoryResponse)
                            .collect(Collectors.toList())
            );
        }
        return categoryResponse;
    }
}
