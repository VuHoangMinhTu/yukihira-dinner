package com.example.demo.exception;

import com.example.demo.dto.AppResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    public AppResponse<String> handleUnauthorizedException(UnauthorizedException e) {
        return AppResponse.<String>builder().code(401).message(e.getMessage()).data(null).build();
    }
    @ExceptionHandler(UserNameNotFoundException.class)
    public AppResponse<String> handleUserNameNotFoundException(UserNameNotFoundException e) {
        return AppResponse.<String>builder().code(404).message(e.getMessage()).data(null).build();
    }
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<AppResponse<String>> handleApplicationException(ApplicationException ex) {
        log.error("Application exception: ", ex);
        var response = AppResponse.<String>builder()
                .message(ex.getErrorCode().getMessage())
                .code(ex.getErrorCode().getCode())
                .build();

        return ResponseEntity.status(ex.getErrorCode().getStatusCode()).body(response);
    }
}
