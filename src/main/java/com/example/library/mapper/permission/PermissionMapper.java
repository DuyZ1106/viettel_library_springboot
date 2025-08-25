package com.example.library.mapper.permission;

import com.example.library.dto.permission.request.CreatePermissionRequest;
import com.example.library.dto.permission.request.UpdatePermissionRequest;
import com.example.library.dto.permission.response.PermissionResponse;
import com.example.library.entity.Permission;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionResponse toResponse(Permission permission);

    List<PermissionResponse> toResponseList(List<Permission> permissions);

    @Mapping(target = "id", ignore = true)
    Permission toEntity(CreatePermissionRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Permission entity, UpdatePermissionRequest request);
}
