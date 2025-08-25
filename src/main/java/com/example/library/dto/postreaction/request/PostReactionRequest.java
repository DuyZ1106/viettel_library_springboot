package com.example.library.dto.postreaction.request;

import com.example.library.entity.enums.ReactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request cho thao tác react (LIKE/DISLIKE) lên 1 Post.
 * - UserId và PostId sẽ lấy từ security context và/hoặc path param, không đặt trong request body.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostReactionRequest {

    @NotNull(message = "reaction.type_required")
    private ReactionType type; // LIKE hoặc DISLIKE
}
