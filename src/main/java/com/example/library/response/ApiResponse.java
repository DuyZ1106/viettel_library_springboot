package com.example.library.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Định dạng chuẩn cho toàn bộ phản hồi từ API.
 * @param <T> kiểu dữ liệu của trường `data`.
 */
@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {

    private String code;    // "success" hoặc mã lỗi (error.user.not_found, ...)
    private String message; // Thông điệp đi kèm
    private T data;         // Payload chính

    // ✅ Success with data
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", "Success", data);
    }

    // ✅ Success without data
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>("success", "Success", null);
    }

    // ✅ Error response
    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
