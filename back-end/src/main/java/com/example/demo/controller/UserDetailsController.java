package com.example.demo.controller;

import com.example.demo.dto.AppResponse;
import com.example.demo.dto.request.ResetPasswordRequestDTO;
import com.example.demo.dto.request.UserDetailsDTO;
import com.example.demo.dto.response.UserDetailsResponseDTO;
import com.example.demo.entity.UserDetails;
import com.example.demo.entity.Users;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.IUserDetailsService;
import com.example.demo.service.UserService;
import com.example.demo.utils.GetCurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-details")
@RequiredArgsConstructor
public class UserDetailsController {
    private final IUserDetailsService userDetailsService;
    private final UserService userService;
    private final UserRepository userRepository;
    @PostMapping("/add")
    public AppResponse<String> add(@RequestBody UserDetailsDTO userDetails) {
        Users user = getUser();
        try {
            boolean result = userDetailsService.addUserDetails(UserDetails.builder()
                    .fullname(userDetails.getFullName())
                    .dob(userDetails.getDateOfBirth())
                    .phoneNum(userDetails.getPhoneNum())
                    .users(user)
                    .build(), user.getUserId());
            return AppResponse.<String>builder().data("Add success").build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return AppResponse.<String>builder()
                    .data("Thêm chi tiết thông tin người dùng thất bại: " + ex.getMessage())
                    .build();
        }
    }

    @GetMapping
    public AppResponse<UserDetailsResponseDTO> getUserDetails() {
        Users user = getUser();
       if (user == null) {
           return AppResponse.<UserDetailsResponseDTO>builder().data(null).build();
       }
        UserDetails userDetails = userDetailsService.getUserDetailsByUserId(user.getUserId());
        if(userDetails == null){
            return AppResponse.<UserDetailsResponseDTO>builder().data(null).build();
        }
        UserDetailsResponseDTO userDetailsDTO = UserDetailsResponseDTO.builder()
                .fullName(userDetails.getFullname())
                .dateOfBirth(userDetails.getDob())
                .phoneNum(userDetails.getPhoneNum())
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRoles().name())
                .build();
        return AppResponse.<UserDetailsResponseDTO>builder().data(userDetailsDTO).build();
    }

    @PutMapping
    public AppResponse<String> update(@RequestBody UserDetailsDTO userDetails) {
        Users user = getUser();
        boolean result = userDetailsService.updateUserDetails(UserDetails.builder()
                .fullname(userDetails.getFullName())
                .dob(userDetails.getDateOfBirth())
                .phoneNum(userDetails.getPhoneNum())
                .users(user)
                .build(), user.getUserId());
        return AppResponse.<String>builder().data("Update success").build();
    }
    @PutMapping("/reset-password")
    public AppResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        String username = GetCurrentUser.getCurrentUsername();
        Long id = userRepository.findByUsername(username).get().getUserId();
        userDetailsService.resetPassword(id, request);

        return AppResponse.<Void>builder().message("Đổi mật khẩu thành công").build();
    }
    private Users getUser() {
        String userName = GetCurrentUser.getCurrentUsername();
        System.out.println("Có username ko: " + userName);
        Users user = userService.getUserByUserName(userName);
        return user;
    }
}
