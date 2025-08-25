package com.example.library.repository;

import com.example.library.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    boolean existsByPostCode(String postCode);

    Optional<Post> findByIdAndIsDeletedFalse(Long id);

    Optional<Post> findByIdAndAuthorIdAndIsDeletedFalse(Long id, Long authorId);
}
