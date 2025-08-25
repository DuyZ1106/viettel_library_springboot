package com.example.library.mapper.comment;

import com.example.library.dto.comment.response.CommentResponse;
import com.example.library.entity.Comment;
import com.example.library.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "author", expression = "java(toAuthorBrief(entity.getAuthor()))")
    @Mapping(target = "children", ignore = true) // sẽ gán ở service khi build tree
    CommentResponse toResponse(Comment entity);

    default CommentResponse.AuthorBrief toAuthorBrief(User user) {
        if (user == null) return null;
        return CommentResponse.AuthorBrief.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .build();
    }
}
