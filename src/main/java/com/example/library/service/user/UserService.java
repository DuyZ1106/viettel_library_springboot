package com.example.library.service.user;

import com.example.library.dto.user.request.CreateUserRequest;
import com.example.library.dto.user.request.UpdateUserRequest;
import com.example.library.dto.user.request.UserSearchRequest;
import com.example.library.dto.user.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id);

    UserResponse getUserById(Long id);

    Page<UserResponse> searchUsers(UserSearchRequest request, Pageable pageable);
}
