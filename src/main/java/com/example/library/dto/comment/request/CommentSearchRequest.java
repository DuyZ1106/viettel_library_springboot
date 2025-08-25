package com.example.library.dto.comment.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentSearchRequest {

    /** Lọc theo post cụ thể */
    private Long postId;

    /** Lọc theo parent comment cụ thể (để lấy reply trực tiếp) */
    private Long parentId;

    /** Tìm kiếm theo nội dung */
    private String keyword;
}
