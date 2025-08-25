package com.example.library.repository;

import com.example.library.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    List<Comment> findByPostIdAndIsDeletedFalseOrderByPathAsc(Long postId);

    long countByPostIdAndIsDeletedFalse(Long postId);

    Optional<Comment> findByIdAndIsDeletedFalse(Long id);

    Optional<Comment> findByIdAndAuthorIdAndIsDeletedFalse(Long id, Long authorId);
}
