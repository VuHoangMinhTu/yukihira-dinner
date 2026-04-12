package com.example.demo.dto.request;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterUserDTO {
    private String username;
    private String password;
    private String fullName;
    private String phoneNum;
    private String email;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
}
