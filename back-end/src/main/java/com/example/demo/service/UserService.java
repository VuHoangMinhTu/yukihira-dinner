package com.example.demo.service;

import com.example.demo.constant.ErrorCode;
import com.example.demo.constant.UserRole;
import com.example.demo.dto.request.LoginUserDTO;
import com.example.demo.dto.request.RegisterUserDTO;
import com.example.demo.entity.UserDetails;
import com.example.demo.entity.Users;
import com.example.demo.exception.ApplicationException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserDetailsRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTToken jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsRepository userDetailsRepository;

    @Transactional
    public String registerUser(RegisterUserDTO requestDTO) {
        // check if user is existed
        try {
            var oUser = userRepository.findByUsername(requestDTO.getUsername());
            if (oUser.isPresent()) {
                if (oUser.get().isVerified()) {
                    return "Tài khoản này đã có hệ thống, vui lòng đăng ký bằng 1 tài khoản khác";
                } else {
                    Users user = oUser.get();
                    user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
                    userRepository.save(user);
                    Optional<UserDetails> oDetails = userDetailsRepository.findById(user.getUserId());
                    UserDetails userDetails = oDetails.orElseGet(() -> UserDetails.builder().users(user).build());

                    userDetails.setDob(requestDTO.getDateOfBirth());
                    userDetails.setFullname(requestDTO.getFullName());
                    userDetails.setPhoneNum(requestDTO.getPhoneNum());
                    userDetailsRepository.save(userDetails);
                    return "Đăng ký tài khoản thành công. Vui lòng xác minh tài khoản";
                }
            }
            Users userHasSameEmail = userRepository.findByEmail(requestDTO.getEmail()).get();
            if (userHasSameEmail != null) {
                return "Email này đã được sử dụng. Vui lòng sử dụng email khác";
            }

            var user = userMapper.toEntity(requestDTO);
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
            user.setRoles(UserRole.CUSTOMER);
            user.setVerified(false);
            user.setEmail(requestDTO.getEmail());
            user.setCreated_date(LocalDate.now());
            userRepository.save(user);
            UserDetails userDetails = UserDetails.builder()
                    .dob(requestDTO.getDateOfBirth())
                    .fullname(requestDTO.getFullName())
                    .phoneNum(requestDTO.getPhoneNum())

                    .users(user)
                    .build();
            userDetailsRepository.save(userDetails);
            return "Đăng ký tài khoản thành công. Vui lòng xác minh tài khoản";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String verify(LoginUserDTO requestDTO) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getUsername(),
                        requestDTO.getPassword()));
        if (authentication.isAuthenticated()) {

            return jwtService.generateToken(authentication);
        } else {
            throw new ApplicationException(ErrorCode.USER_NOT_EXISTED);
        }
    }

    public String verifyAccountAdmin(LoginUserDTO requestDTO) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getUsername(),
                        requestDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            Optional<Users> user = userRepository.findByUsername(requestDTO.getUsername());
            if (user.isPresent() && (user.get().getRoles().equals(UserRole.ADMIN) || user.get().getRoles().equals(UserRole.MANAGER))) {
                System.out.println("User role: " + user.get().getRoles());
                return jwtService.generateToken(authentication);

            } else {
                throw new ApplicationException(ErrorCode.UNAUTHORIZED);
            }
        } else {
            throw new ApplicationException(ErrorCode.USER_NOT_EXISTED);
        }
    }

    public Users getUserByUserName(String username) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHENTICATED));
        return user;
    }

}
