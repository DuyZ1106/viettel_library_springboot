package com.example.library.dto.rolegroup.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateRoleGroupRequest {

    @NotBlank
    private String roleGroupName;

    private String description;

    private List<Long> userIds;         // ✅ thêm dòng này
    private List<Long> permissionIds;   // ✅ thêm dòng này
}
