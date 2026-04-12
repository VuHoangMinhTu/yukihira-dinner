package com.example.demo.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDTO {
    private String username;
    private String password;
}
