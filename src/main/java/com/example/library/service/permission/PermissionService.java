package com.example.library.service.permission;

import com.example.library.dto.permission.request.CreatePermissionRequest;
import com.example.library.dto.permission.request.UpdatePermissionRequest;
import com.example.library.dto.permission.request.SearchPermissionRequest;
import com.example.library.dto.permission.response.PermissionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PermissionService {

    PermissionResponse create(CreatePermissionRequest request);

    PermissionResponse update(Long id, UpdatePermissionRequest request);

    void delete(Long id);

    PermissionResponse getById(Long id);

    Page<PermissionResponse> search(SearchPermissionRequest request, Pageable pageable);

    List<PermissionResponse> findPermissionsByUserId(Long userId);
}
