package com.example.library.dto.post.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequest {

    /** Cho phép cập nhật từng phần (partial update) */
    @Size(max = 255, message = "error.post.title.max_length")
    private String title;

    private String content;

    @Size(max = 255, message = "error.post.publisher.max_length")
    private String publisher;

    private Long bookId;

    /** Bật/tắt hiển thị; xóa mềm xử lý ở service, không nằm trong DTO này */
    private Boolean isActive;
}
