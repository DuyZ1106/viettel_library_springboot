package com.example.library.entity;

import com.example.library.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts", indexes = {
        @Index(name = "idx_posts_author", columnList = "author_id"),
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = {"author", "book"})
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Mã code duy nhất cho post (để hiển thị/tra cứu) */
    @Column(name = "post_code", nullable = false, unique = true, length = 50)
    private String postCode;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    private String content;

    @Column(length = 255)
    private String publisher;

    /** Nếu bài viết nói về 1 quyển sách */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    /** Tác giả bài viết */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /** Counters (khởi tạo 0 để tránh null) */
    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L;

    @Column(name = "dislike_count", nullable = false)
    private Long dislikeCount = 0L;

    @Column(name = "comment_count", nullable = false)
    private Long commentCount = 0L;
}
