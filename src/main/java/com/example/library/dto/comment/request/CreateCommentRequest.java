package com.example.library.dto.comment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {

    /** Bài viết mà comment thuộc về */
    @NotNull(message = "error.comment.post_required")
    private Long postId;

    /** Nếu là reply cho comment khác */
    private Long parentId;

    /** Nội dung comment */
    @NotBlank(message = "error.comment.content_required")
    private String content;

    /** Trạng thái kích hoạt (mặc định true) */
    private Boolean isActive;
}
