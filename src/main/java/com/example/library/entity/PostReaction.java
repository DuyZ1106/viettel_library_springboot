package com.example.library.entity;

import com.example.library.entity.base.BaseEntity;
import com.example.library.entity.enums.ReactionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_reactions",
        uniqueConstraints = @UniqueConstraint(name = "uk_post_user", columnNames = {"post_id", "user_id"}),
        indexes = {
                @Index(name = "idx_postreaction_user", columnList = "user_id"),
                @Index(name = "idx_postreaction_post_type", columnList = "post_id, type")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = {"post", "user"})
public class PostReaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Bài viết được react */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    /** Người react */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** LIKE / DISLIKE */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ReactionType type;
}
