package com.example.library.controller.user;

import com.example.library.dto.user.request.CreateUserRequest;
import com.example.library.dto.user.request.UpdateUserRequest;
import com.example.library.dto.user.request.UserSearchRequest;
import com.example.library.dto.user.response.UserResponse;
import com.example.library.response.ApiResponse;
import com.example.library.service.user.UserService;
import com.example.library.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/library/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * ROLE_VIEW_USER - Tìm kiếm danh sách người dùng (phân trang + tìm kiếm nhiều điều kiện)
     * Dùng POST để gửi JSON request body.
     */
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_VIEW_USER')")
    public ApiResponse<Page<UserResponse>> searchUsers(
            @RequestBody(required = false) UserSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (request == null) {
            request = new UserSearchRequest(); // fallback nếu không gửi body
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return ResponseUtil.success(userService.searchUsers(request, pageable)).getBody();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_CREATE_USER')")
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseUtil.success(userService.createUser(request)).getBody();
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasRole('ROLE_VIEW_USER')")
    public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
        return ResponseUtil.success(userService.getUserById(id)).getBody();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_UPDATE_USER')")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseUtil.success(userService.updateUser(id, request)).getBody();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_DELETE_USER')")
    public ApiResponse<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseUtil.success(null).getBody();
    }
}
