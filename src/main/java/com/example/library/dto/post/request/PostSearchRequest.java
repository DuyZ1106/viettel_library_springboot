package com.example.library.dto.post.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchRequest {

    /** Tìm theo từ khóa chung: áp dụng cho title/publisher/content */
    private String keyword;

    /** Lọc theo tác giả cụ thể (author_id) */
    private Long authorId;

    /** Lọc theo book nếu post gắn với sách */
    private Long bookId;

    /** Khoảng thời gian tạo */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdTo;

    /** Chỉ định lọc theo trạng thái hoạt động; nếu null thì không lọc */
    private Boolean isActive;

    /**
     * LƯU Ý:
     * - isDeleted sẽ luôn được service/repository mặc định lọc = false
     *   để đảm bảo soft-delete không hiển thị ở danh sách/chi tiết.
     */
}
