package com.example.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class CategoryResponse {
    private Long id;
    private String name;
    private List<CategoryResponse> categoryChildren = new ArrayList<>();
}
