package com.example.library.controller.category;

import com.example.library.dto.category.request.CreateCategoryRequest;
import com.example.library.dto.category.request.UpdateCategoryRequest;
import com.example.library.dto.category.request.CategorySearchRequest;
import com.example.library.dto.category.response.CategoryResponse;
import com.example.library.response.ApiResponse;
import com.example.library.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/library.category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // API: Danh sách - ROLE_VIEW_CATEGORY
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW_CATEGORY')")
    public ApiResponse<Page<CategoryResponse>> searchCategories(
            @ModelAttribute CategorySearchRequest request,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ApiResponse.success(categoryService.searchCategories(request, pageable));
    }

    // API: Thêm mới - ROLE_CREATE_CATEGORY
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_CATEGORY')")
    public ApiResponse<CategoryResponse> createCategory(
            @Valid @RequestBody CreateCategoryRequest request
    ) {
        return ApiResponse.success(categoryService.createCategory(request));
    }

    // API: Xem chi tiết - ROLE_VIEW_CATEGORY
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_CATEGORY')")
    public ApiResponse<CategoryResponse> getCategory(@PathVariable Long id) {
        return ApiResponse.success(categoryService.getCategoryById(id));
    }

    // API: Cập nhật - ROLE_UPDATE_CATEGORY
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_CATEGORY')")
    public ApiResponse<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        return ApiResponse.success(categoryService.updateCategory(id, request));
    }

    // API: Xóa - ROLE_DELETE_CATEGORY
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_CATEGORY')")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.success();
    }
}
