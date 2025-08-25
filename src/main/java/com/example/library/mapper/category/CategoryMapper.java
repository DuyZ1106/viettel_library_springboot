package com.example.library.mapper.category;

import com.example.library.dto.category.request.CreateCategoryRequest;
import com.example.library.dto.category.request.UpdateCategoryRequest;
import com.example.library.dto.category.response.CategoryResponse;
import com.example.library.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // Entity -> Response
    @Mapping(source = "categoryName", target = "name")
    CategoryResponse toResponse(Category category);

    @Mapping(source = "categoryName", target = "name")
    List<CategoryResponse> toResponseList(List<Category> categories);

    // Create Request -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "categoryName")
    Category toEntity(CreateCategoryRequest request);

    // Update Request -> Entity (chỉ cập nhật khi không null)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "name", target = "categoryName")
    void updateEntity(@MappingTarget Category category, UpdateCategoryRequest request);
}
