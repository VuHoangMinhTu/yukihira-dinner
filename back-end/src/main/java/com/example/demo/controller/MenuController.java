package com.example.demo.controller;

import com.example.demo.dto.AppResponse;
import com.example.demo.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.CategoryService;
import java.util.List;
@RequestMapping("/api/menu")
@RestController
@RequiredArgsConstructor
public class MenuController {
    private final CategoryService categoryService;
  @GetMapping("/get-all")
  public AppResponse<List<CategoryResponse>> getAllMenu() {
      List<CategoryResponse> result = categoryService.getAllCategories();
      if (result != null) {
          return AppResponse.<List<CategoryResponse>>builder().data(result).build();
      }

      return AppResponse.<List<CategoryResponse>>builder().message("Lỗi khi lấy data").code(999).data(null).build(); }

}
