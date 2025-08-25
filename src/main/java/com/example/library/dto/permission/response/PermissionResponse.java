package com.example.library.dto.permission.response;

import lombok.Data;

@Data
public class PermissionResponse {
    private Long id;
    private String permissionCode;
    private String permissionName;
    private String description;
}
