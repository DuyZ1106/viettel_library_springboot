package com.example.library.repository;

import com.example.library.entity.PostReaction;
import com.example.library.entity.enums.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {

    Optional<PostReaction> findByPostIdAndUserId(Long postId, Long userId);

    long countByPostIdAndType(Long postId, ReactionType type);
}
