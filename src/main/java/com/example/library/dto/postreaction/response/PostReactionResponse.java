package com.example.library.dto.postreaction.response;

import com.example.library.entity.enums.ReactionType;
import lombok.Builder;
import lombok.Data;

/**
 * Response DTO cho PostReaction.
 * Dùng để trả về trạng thái reaction của user trên 1 post.
 */
@Data
@Builder
public class PostReactionResponse {

    private Long postId;
    private Long userId;
    private ReactionType type; // LIKE hoặc DISLIKE
}
