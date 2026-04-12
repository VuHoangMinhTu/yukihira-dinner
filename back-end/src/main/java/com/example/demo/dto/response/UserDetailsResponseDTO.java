package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class UserDetailsResponseDTO {
    Long userId;
    String fullName;
    String gender;
    String phoneNum;
    Boolean verified;
    String email;
    String username;
    String status;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate dateOfBirth;
    String role;

}
