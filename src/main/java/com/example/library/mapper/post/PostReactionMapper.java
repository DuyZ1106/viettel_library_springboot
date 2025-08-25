package com.example.library.mapper.post;

import com.example.library.dto.postreaction.request.PostReactionRequest;
import com.example.library.dto.postreaction.response.PostReactionResponse;
import com.example.library.entity.PostReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostReactionMapper {

    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "userId", source = "user.id")
    PostReactionResponse toResponse(PostReaction entity);

    List<PostReactionResponse> toResponses(List<PostReaction> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void updateEntity(@MappingTarget PostReaction entity, PostReactionRequest request);
}
