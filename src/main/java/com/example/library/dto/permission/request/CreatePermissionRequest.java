package com.example.library.dto.permission.request;

import lombok.Data;

@Data
public class CreatePermissionRequest {
    private String permissionCode;
    private String permissionName;
    private String description;
}
