package com.example.library.controller.rolegroup;

import com.example.library.dto.rolegroup.request.CreateRoleGroupRequest;
import com.example.library.dto.rolegroup.request.UpdateRoleGroupRequest;
import com.example.library.dto.rolegroup.request.RoleGroupSearchRequest;
import com.example.library.dto.rolegroup.response.RoleGroupResponse;
import com.example.library.response.ApiResponse;
import com.example.library.service.rolegroup.RoleGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/library/role")
@RequiredArgsConstructor
public class RoleGroupController {

    private final RoleGroupService roleGroupService;

    /**
     * Danh sách nhóm quyền (search + paging)
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_VIEW_ROLE_GROUP')")
    public ResponseEntity<ApiResponse<Page<RoleGroupResponse>>> search(
            @RequestBody RoleGroupSearchRequest request,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success(roleGroupService.search(request, pageable)));
    }

    /**
     * Thêm mới nhóm quyền
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_ROLE_GROUP')")
    public ResponseEntity<ApiResponse<RoleGroupResponse>> create(
            @RequestBody @Valid CreateRoleGroupRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(roleGroupService.create(request)));
    }

    /**
     * Xem chi tiết nhóm quyền
     */
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_ROLE_GROUP')")
    public ResponseEntity<ApiResponse<RoleGroupResponse>> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(ApiResponse.success(roleGroupService.getById(id)));
    }

    /**
     * Cập nhật nhóm quyền
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_ROLE_GROUP')")
    public ResponseEntity<ApiResponse<RoleGroupResponse>> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateRoleGroupRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(roleGroupService.update(id, request)));
    }

    /**
     * Xóa nhóm quyền (soft delete)
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_ROLE_GROUP')")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        roleGroupService.delete(id);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
