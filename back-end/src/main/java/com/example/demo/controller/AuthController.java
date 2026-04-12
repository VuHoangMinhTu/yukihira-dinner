package com.example.demo.controller;

import com.example.demo.constant.EUserStatus;
import com.example.demo.dto.AppResponse;
import com.example.demo.dto.request.LoginUserDTO;
import com.example.demo.dto.request.RegisterUserDTO;
import com.example.demo.dto.request.VerifyOTPRequestDTO;
import com.example.demo.entity.Users;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.OtpRedisService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmailService emailService;

    private final OtpRedisService otpService;
        private final UserService userService;

        private final UserRepository userRepository;


        @PostMapping("/register")
        public AppResponse<String> register(@RequestBody RegisterUserDTO requestDTO) {
            String result = userService.registerUser(requestDTO);
            if (result.equals("Tài khoản này đã có hệ thống, vui lòng đăng ký bằng 1 tài khoản khác")) {
                return AppResponse.<String>builder()
                        .data(result)
                        .build();
            }
            if (result.equals("Email này đã được sử dụng. Vui lòng sử dụng email khác")) {
                return AppResponse.<String>builder()
                        .data(result)
                        .build();
            }
            if (result.equals("Đăng ký tài khoản thành công. Vui lòng xác minh tài khoản")) {
                return AppResponse.<String>builder()
                        .data(result)
                        .build();
            }
            return AppResponse.<String>builder()
                    .data("Đăng ký tài khoản bị lỗi. Vui lòng thử lại sau")
                    .build();

        }

        @PostMapping("register/send-otp")
        public AppResponse<String> sendOtp(@RequestBody Map<String, String> body) {
            try {
                String email = body.get("email");
                String otp = generateOtp();
                emailService.sendOtpEmail(email, "OTP đăng ký", otp);
                otpService.saveOtp(email, otp);

                return AppResponse.<String>builder().data("Đã gửi OTP tới email.").build();
            } catch (Exception e) {
                e.printStackTrace();
                return AppResponse.<String>builder().data("Lỗi khi gửi OTP.").build();
            }
        }

        @PostMapping("/register/verify-otp")
        public AppResponse<String> verifyOtp(@RequestBody VerifyOTPRequestDTO request) {
            try {
                boolean valid = otpService.isValid(request.getEmail(), request.getOtp());
                if (!valid) {
                    return AppResponse.<String>builder().data("OTP sai hoặc hết hạn.").build();
                }
                otpService.clearOtp(request.getEmail());
                Users user = userRepository.findByEmail(request.getEmail()).get();
                user.setVerified(true);
                user.setStatus(EUserStatus.ACTIVE);
                userRepository.save(user);
                return AppResponse.<String>builder().data("Xác thực OTP thành công!").build();
            } catch (Exception e) {
                e.printStackTrace();
                return AppResponse.<String>builder().data("Email không tồn tại. Vui lòng kiểm tra lại email.").build();
            }
        }

    @PostMapping("/login")
    public AppResponse<String> login(@RequestBody LoginUserDTO requestDTO) {
        var token = userService.verify(requestDTO);
        return AppResponse.<String>builder()
                .data(token)
                .build();
    }

    @PostMapping("/login/admin")
    public AppResponse<String> loginOfAdmin(@RequestBody LoginUserDTO requestDTO) {
        var token = userService.verifyAccountAdmin(requestDTO);
        return AppResponse.<String>builder()
                .data(token)
                .build();
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }



}
