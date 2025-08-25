package com.example.library.service.rolegroup;

import com.example.library.dto.rolegroup.request.CreateRoleGroupRequest;
import com.example.library.dto.rolegroup.request.UpdateRoleGroupRequest;
import com.example.library.dto.rolegroup.request.RoleGroupSearchRequest;
import com.example.library.dto.rolegroup.response.RoleGroupResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleGroupService {

    RoleGroupResponse create(CreateRoleGroupRequest request);

    RoleGroupResponse update(Long id, UpdateRoleGroupRequest request);

    void delete(Long id);

    RoleGroupResponse getById(Long id);

    Page<RoleGroupResponse> search(RoleGroupSearchRequest request, Pageable pageable);

    // ✅ Thêm mới để gán permissions vào role group
    void assignPermissions(Long roleGroupId, List<Long> permissionIds);
}
