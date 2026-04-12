package com.example.demo.service;

import com.example.demo.dto.CreateUserForAdminRequestDTO;
import com.example.demo.dto.FilterUserDetailsRequestDTO;
import com.example.demo.dto.request.ResetPasswordRequestDTO;
import com.example.demo.dto.request.UpdateUserForAdminRequestDTO;
import com.example.demo.dto.response.UserDetailsResponseDTO;
import com.example.demo.entity.UserDetails;
import org.springframework.data.domain.Page;

public interface IUserDetailsService {
    UserDetails getUserDetailsByUserId(Long userId);

    boolean updateUserDetails(UserDetails userDetails, Long userId);

    boolean addUserDetails(UserDetails userDetails, Long userId);

    UserDetailsResponseDTO getUserDetails(Long userId);

    Page<UserDetailsResponseDTO> filterUsers(FilterUserDetailsRequestDTO request, int page, int size);

    UserDetailsResponseDTO createUser(CreateUserForAdminRequestDTO request);

    UserDetailsResponseDTO updateUser(Long userId, UpdateUserForAdminRequestDTO request);

    void resetPassword(Long userId, ResetPasswordRequestDTO request);
}
