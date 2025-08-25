package com.example.library.mapper.rolegroup;

import com.example.library.dto.rolegroup.request.CreateRoleGroupRequest;
import com.example.library.dto.rolegroup.request.UpdateRoleGroupRequest;
import com.example.library.dto.rolegroup.response.RoleGroupResponse;
import com.example.library.entity.RoleGroup;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleGroupMapper {

    // Convert entity → response
    RoleGroupResponse toResponse(RoleGroup roleGroup);

    List<RoleGroupResponse> toResponseList(List<RoleGroup> roleGroups);

    // Convert create request → entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    RoleGroup toEntity(CreateRoleGroupRequest request);

    // Update entity bằng update request (không set null nếu có @BeanMapping)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    void updateEntity(@MappingTarget RoleGroup roleGroup, UpdateRoleGroupRequest request);
}
