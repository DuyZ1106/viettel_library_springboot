package com.example.library.service.impl.permission;

import com.example.library.dto.permission.request.CreatePermissionRequest;
import com.example.library.dto.permission.request.UpdatePermissionRequest;
import com.example.library.dto.permission.request.SearchPermissionRequest;
import com.example.library.dto.permission.response.PermissionResponse;
import com.example.library.entity.Permission;
import com.example.library.entity.RoleGroup;
import com.example.library.entity.User;
import com.example.library.exception.NotFoundException;
import com.example.library.mapper.permission.PermissionMapper;
import com.example.library.repository.PermissionRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.permission.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public PermissionResponse create(CreatePermissionRequest request) {
        Permission permission = permissionMapper.toEntity(request);
        permission.setIsDeleted(false);
        return permissionMapper.toResponse(permissionRepository.save(permission));
    }

    @Override
    public PermissionResponse update(Long id, UpdatePermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .filter(p -> !p.getIsDeleted())
                .orElseThrow(() -> new NotFoundException("permission.not_found", "Permission not found"));

        permissionMapper.updateEntity(permission, request);
        return permissionMapper.toResponse(permissionRepository.save(permission));
    }

    @Override
    public void delete(Long id) {
        Permission permission = permissionRepository.findById(id)
                .filter(p -> !p.getIsDeleted())
                .orElseThrow(() -> new NotFoundException("permission.not_found", "Permission not found"));

        permission.setIsDeleted(true);
        permissionRepository.save(permission);
    }

    @Override
    public PermissionResponse getById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .filter(p -> !p.getIsDeleted())
                .orElseThrow(() -> new NotFoundException("permission.not_found", "Permission not found"));
        return permissionMapper.toResponse(permission);
    }

    @Override
    public Page<PermissionResponse> search(SearchPermissionRequest request, Pageable pageable) {
        List<Permission> all = permissionRepository.findAll();
        List<Permission> filtered = all.stream()
                .filter(p -> !p.getIsDeleted()
                        && (StringUtils.isEmpty(request.getKeyword())
                        || p.getPermissionCode().toLowerCase().contains(request.getKeyword().toLowerCase())
                        || p.getPermissionName().toLowerCase().contains(request.getKeyword().toLowerCase())))
                .toList();

        int total = filtered.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);
        List<PermissionResponse> content = permissionMapper.toResponseList(filtered.subList(start, end));

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<PermissionResponse> findPermissionsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user.not_found", "User not found"));

        RoleGroup roleGroup = user.getRoleGroup();
        Set<Permission> permissions = roleGroup.getPermissions().stream()
                .filter(p -> !p.getIsDeleted())
                .collect(Collectors.toSet());

        return permissionMapper.toResponseList(permissions.stream().toList());
    }
}
