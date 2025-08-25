package com.example.library.entity;

import com.example.library.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments", indexes = {
        @Index(name = "idx_comments_post_path", columnList = "post_id, path"),
        @Index(name = "idx_comments_post_parent", columnList = "post_id, parent_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = {"post", "author", "parent"})
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Bài viết mà comment thuộc về */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    /** Tác giả comment */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /** Comment cha (nếu là reply) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    /** Nội dung */
    @Column(length = 1000, nullable = false)
    private String content;

    /** Materialized-path, ví dụ: 0000000001/0000000005 */
    @Column(nullable = false, length = 255)
    private String path;

    /** Độ sâu trong cây */
    @Column(name = "level", nullable = false)
    private Integer level = 0;
}
