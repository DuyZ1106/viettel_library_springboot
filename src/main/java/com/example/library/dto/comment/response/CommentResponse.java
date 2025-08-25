package com.example.library.dto.comment.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private Long postId;
    private Long parentId;
    private Integer level;
    private String path;
    private String content;

    private AuthorBrief author;

    /** Nếu trả về dạng cây, chứa danh sách reply */
    private List<CommentResponse> children;

    /** Audit */
    private Boolean isActive;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Long version;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorBrief {
        private Long id;
        private String username;
        private String fullName;
    }
}
