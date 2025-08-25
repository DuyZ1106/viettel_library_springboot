package com.example.library.util;

import com.example.library.response.ApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.*;

public class ResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    public static ResponseEntity<ApiResponse<?>> error(String code, String message) {
        return ResponseEntity.badRequest().body(ApiResponse.error(code, message));
    }

    public static ResponseEntity<Resource> download(Resource resource, String filename) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
}
