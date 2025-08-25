package com.example.library.service.impl.rolegroup;

import com.example.library.dto.rolegroup.request.CreateRoleGroupRequest;
import com.example.library.dto.rolegroup.request.UpdateRoleGroupRequest;
import com.example.library.dto.rolegroup.request.RoleGroupSearchRequest;
import com.example.library.dto.rolegroup.response.RoleGroupResponse;
import com.example.library.entity.Permission;
import com.example.library.entity.RoleGroup;
import com.example.library.entity.User;
import com.example.library.exception.BusinessException;
import com.example.library.exception.NotFoundException;
import com.example.library.mapper.rolegroup.RoleGroupMapper;
import com.example.library.repository.PermissionRepository;
import com.example.library.repository.RoleGroupRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.rolegroup.RoleGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleGroupServiceImpl implements RoleGroupService {

    private final RoleGroupRepository roleGroupRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final RoleGroupMapper roleGroupMapper;

    @Override
    public RoleGroupResponse create(CreateRoleGroupRequest request) {
        RoleGroup roleGroup = new RoleGroup();
        roleGroup.setRoleGroupCode(request.getRoleGroupCode());
        roleGroup.setRoleGroupName(request.getRoleGroupName());
        roleGroup.setDescription(request.getDescription());
        roleGroup.setIsDeleted(false);

        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            List<Permission> permissions = permissionRepository.findAllById(request.getPermissionIds());
            roleGroup.setPermissions(new HashSet<>(permissions));
        }

        if (request.getUserIds() != null && !request.getUserIds().isEmpty()) {
            List<User> users = userRepository.findAllById(request.getUserIds());
            for (User user : users) {
                user.setRoleGroup(roleGroup);
            }
            roleGroup.setUsers(new HashSet<>(users));
        }

        RoleGroup saved = roleGroupRepository.save(roleGroup);
        return roleGroupMapper.toResponse(saved);
    }

    @Override
    public RoleGroupResponse update(Long id, UpdateRoleGroupRequest request) {
        RoleGroup roleGroup = roleGroupRepository.findById(id)
                .filter(rg -> !rg.getIsDeleted())
                .orElseThrow(() -> new NotFoundException("role_group.not_found", "Role group not found"));

        roleGroupMapper.updateEntity(roleGroup, request);

        if (request.getPermissionIds() != null) {
            List<Permission> permissions = permissionRepository.findAllById(request.getPermissionIds());
            roleGroup.setPermissions(new HashSet<>(permissions));
        }

        if (request.getUserIds() != null) {
            List<User> users = userRepository.findAllById(request.getUserIds());

            List<User> existingUsers = userRepository.findByRoleGroupId(roleGroup.getId());
            for (User user : existingUsers) {
                user.setRoleGroup(null);
            }

            for (User user : users) {
                user.setRoleGroup(roleGroup);
            }
            roleGroup.setUsers(new HashSet<>(users));
        }

        RoleGroup saved = roleGroupRepository.save(roleGroup);
        return roleGroupMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        RoleGroup roleGroup = roleGroupRepository.findById(id)
                .filter(rg -> !rg.getIsDeleted())
                .orElseThrow(() -> new NotFoundException("role_group.not_found", "Role group not found"));
        roleGroup.setIsDeleted(true);
        roleGroupRepository.save(roleGroup);
    }

    @Override
    public RoleGroupResponse getById(Long id) {
        RoleGroup roleGroup = roleGroupRepository.findById(id)
                .filter(rg -> !rg.getIsDeleted())
                .orElseThrow(() -> new NotFoundException("role_group.not_found", "Role group not found"));

        return roleGroupMapper.toResponse(roleGroup);
    }

    @Override
    public Page<RoleGroupResponse> search(RoleGroupSearchRequest request, Pageable pageable) {
        List<RoleGroup> all = roleGroupRepository.findAll();

        List<RoleGroup> filtered = all.stream()
                .filter(rg -> !rg.getIsDeleted()
                        && (StringUtils.isEmpty(request.getKeyword())
                        || rg.getRoleGroupCode().toLowerCase().contains(request.getKeyword().toLowerCase())
                        || rg.getRoleGroupName().toLowerCase().contains(request.getKeyword().toLowerCase())))
                .toList();

        int total = filtered.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);
        List<RoleGroupResponse> content = roleGroupMapper.toResponseList(filtered.subList(start, end));

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public void assignPermissions(Long roleGroupId, List<Long> permissionIds) {
        RoleGroup roleGroup = roleGroupRepository.findById(roleGroupId)
                .filter(rg -> !rg.getIsDeleted())
                .orElseThrow(() -> new NotFoundException("role_group.not_found", "Role group not found"));

        List<Permission> permissions = permissionRepository.findAllById(permissionIds);

        if (permissions.size() != permissionIds.size()) {
            throw new BusinessException("permission.invalid", "Some permission IDs are invalid");
        }

        roleGroup.setPermissions(new HashSet<>(permissions));
        roleGroupRepository.save(roleGroup);
    }
}
