package com.example.library.mapper.user;

import com.example.library.dto.user.request.CreateUserRequest;
import com.example.library.dto.user.request.UpdateUserRequest;
import com.example.library.dto.user.response.UserResponse;
import com.example.library.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Convert entity → response
    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    // Convert create request → entity
    @Mapping(target = "id", ignore = true)
    User toEntity(CreateUserRequest request);

    // Update entity bằng update request (không set null nếu có @BeanMapping)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget User user, UpdateUserRequest request);
}
