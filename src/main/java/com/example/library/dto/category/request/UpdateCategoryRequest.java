package com.example.library.dto.category.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryRequest {

    @NotNull
    private Long id;

    private String name;

    private String description;
}
