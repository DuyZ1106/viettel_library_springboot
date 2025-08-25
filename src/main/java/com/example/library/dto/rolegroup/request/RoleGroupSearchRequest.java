package com.example.library.dto.rolegroup.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleGroupSearchRequest {
    private String keyword; // Tìm theo code hoặc name
}
