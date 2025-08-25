package com.example.library.mapper.post;

import com.example.library.dto.post.request.CreatePostRequest;
import com.example.library.dto.post.request.UpdatePostRequest;
import com.example.library.dto.post.response.PostResponse;
import com.example.library.entity.Post;
import com.example.library.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "author", expression = "java(toAuthorBrief(entity.getAuthor()))")
    @Mapping(target = "bookId", source = "book.id")
    PostResponse toResponse(Post entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "postCode", ignore = true)
    @Mapping(target = "likeCount", constant = "0L")
    @Mapping(target = "dislikeCount", constant = "0L")
    @Mapping(target = "commentCount", constant = "0L")
    Post toEntity(CreatePostRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "postCode", ignore = true)
    void updateEntity(@MappingTarget Post entity, UpdatePostRequest request);

    // -------- helpers ----------
    default PostResponse.AuthorBrief toAuthorBrief(User user) {
        if (user == null) return null;
        return PostResponse.AuthorBrief.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .build();
    }
}
