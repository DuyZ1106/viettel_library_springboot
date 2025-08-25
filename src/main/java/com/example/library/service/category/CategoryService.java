package com.example.library.service.category;

import com.example.library.dto.category.request.CreateCategoryRequest;
import com.example.library.dto.category.request.UpdateCategoryRequest;
import com.example.library.dto.category.request.CategorySearchRequest;
import com.example.library.dto.category.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryResponse createCategory(CreateCategoryRequest request);

    CategoryResponse updateCategory(Long id, UpdateCategoryRequest request);

    void deleteCategory(Long id);

    CategoryResponse getCategoryById(Long id);

    Page<CategoryResponse> searchCategories(CategorySearchRequest request, Pageable pageable);
}
