package com.example.demo.mapper;

import com.example.demo.dto.request.RegisterUserDTO;
import com.example.demo.entity.Users;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.security.core.userdetails.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface UserMapper {
    Users toEntity(RegisterUserDTO dto);
}
