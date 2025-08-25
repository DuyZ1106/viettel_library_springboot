package com.example.library.dto.rolegroup.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoleGroupResponse {
    private Long id;
    private String roleGroupCode;
    private String roleGroupName;
    private String description;
}
