package com.example.library.dto.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {

    /** Tiêu đề bắt buộc */
    @NotBlank(message = "error.post.title.required")
    @Size(max = 255, message = "error.post.title.max_length")
    private String title;

    /** Nội dung bài viết */
    @NotBlank(message = "error.post.content.required")
    private String content;

    /** Nhà xuất bản / nguồn tác giả muốn ghi chú (không bắt buộc) */
    @Size(max = 255, message = "error.post.publisher.max_length")
    private String publisher;

    /** Nếu post nói về 1 quyển sách cụ thể thì truyền bookId (không bắt buộc) */
    private Long bookId;

    /** Cho phép bật/tắt hiển thị (mặc định true nếu không truyền) */
    private Boolean isActive;
}
