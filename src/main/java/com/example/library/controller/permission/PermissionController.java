package com.example.library.controller.permission;

import com.example.library.dto.permission.request.CreatePermissionRequest;
import com.example.library.dto.permission.request.UpdatePermissionRequest;
import com.example.library.dto.permission.request.SearchPermissionRequest;
import com.example.library.dto.permission.response.PermissionResponse;
import com.example.library.response.ApiResponse;
import com.example.library.service.permission.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/library/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * Danh sách permission (search + paging)
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW_PERMISSION')")
    public ResponseEntity<ApiResponse<Page<PermissionResponse>>> search(
            @RequestBody SearchPermissionRequest request,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.search(request, pageable)));
    }

    /**
     * Thêm mới permission
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_PERMISSION')")
    public ResponseEntity<ApiResponse<PermissionResponse>> create(
            @RequestBody @Valid CreatePermissionRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.create(request)));
    }

    /**
     * Xem chi tiết permission
     */
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_PERMISSION')")
    public ResponseEntity<ApiResponse<PermissionResponse>> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.getById(id)));
    }

    /**
     * Cập nhật permission
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_PERMISSION')")
    public ResponseEntity<ApiResponse<PermissionResponse>> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdatePermissionRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.update(id, request)));
    }

    /**
     * Xóa permission (soft delete)
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_PERMISSION')")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /**
     * Xem danh sách quyền của user
     */
    @GetMapping("/find-by-user/{userId}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_PERMISSION')")
    public ResponseEntity<ApiResponse<?>> findPermissionsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.findPermissionsByUserId(userId)));
    }
}
