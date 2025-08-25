package com.example.library.dto.permission.request;

import lombok.Data;

@Data
public class UpdatePermissionRequest {

    private String permissionName;

    private String description;
}
