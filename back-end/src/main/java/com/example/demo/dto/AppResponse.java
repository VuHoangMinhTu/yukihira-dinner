package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Builder
public class AppResponse<T> {
    @Builder.Default
    private int code = 200;
    private T data;
    @Builder.Default
    private String message = "Success";
}
