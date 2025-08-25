package com.example.library.exception;

import com.example.library.response.ApiResponse;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

/**
 * Bắt lỗi toàn cục cho REST API:
 * - AuthenticationException ➜ 401
 * - Các lỗi khác            ➜ 500
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Lỗi xác thực (sai mật khẩu, user không tồn tại, token hỏng…) */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuth(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("error.auth", ex.getMessage()));
    }

    /** Lỗi chưa xử lý khác */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleOther(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("error.internal", "Something went wrong"));
    }
}
