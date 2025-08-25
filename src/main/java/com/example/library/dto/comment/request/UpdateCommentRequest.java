package com.example.library.dto.comment.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentRequest {

    /** Nội dung mới */
    private String content;

    /** Bật/tắt hiển thị */
    private Boolean isActive;
}
