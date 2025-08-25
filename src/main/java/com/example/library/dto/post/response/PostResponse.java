package com.example.library.dto.post.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String postCode;

    private String title;
    private String content;
    private String publisher;

    /** Tham chiếu sách (nếu có) */
    private Long bookId;

    /** Thông tin tác giả rút gọn */
    private AuthorBrief author;

    /** Thống kê phản ứng & bình luận */
    private Long likeCount;
    private Long dislikeCount;
    private Long commentCount;

    /** Audit */
    private Boolean isActive;
    private Boolean isDeleted; // luôn false trên list/detail bình thường
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
        private String fullName; // nếu User có trường này; nếu không có, có thể bỏ
    }
}
