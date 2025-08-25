package com.example.library.service.impl.category;

import com.example.library.dto.category.request.CreateCategoryRequest;
import com.example.library.dto.category.request.UpdateCategoryRequest;
import com.example.library.dto.category.request.CategorySearchRequest;
import com.example.library.dto.category.response.CategoryResponse;
import com.example.library.entity.Category;
import com.example.library.exception.BusinessException;
import com.example.library.mapper.category.CategoryMapper;
import com.example.library.repository.CategoryRepository;
import com.example.library.service.category.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static org.springframework.util.StringUtils.hasText;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsByCategoryNameIgnoreCase(request.getName())) {
            throw new BusinessException("error.category.name.exists");
        }

        Category category = categoryMapper.toEntity(request);
        category.setIsDeleted(false);
        category.setIsActive(true);

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .filter(c -> !Boolean.TRUE.equals(c.getIsDeleted()))
                .orElseThrow(() -> new BusinessException("error.category.not_found"));

        categoryMapper.updateEntity(category, request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .filter(c -> !Boolean.TRUE.equals(c.getIsDeleted()))
                .orElseThrow(() -> new BusinessException("error.category.not_found"));

        category.setIsDeleted(true);
        category.setIsActive(false);
        categoryRepository.save(category);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .filter(c -> !Boolean.TRUE.equals(c.getIsDeleted()))
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new BusinessException("error.category.not_found"));
    }

    @Override
    public Page<CategoryResponse> searchCategories(CategorySearchRequest request, Pageable pageable) {
        Specification<Category> spec = Specification.where((root, query, cb) -> cb.isFalse(root.get("isDeleted")));

        if (hasText(request.getName())) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%"));
        }
        if (hasText(request.getDescription())) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("description")), "%" + request.getDescription().toLowerCase() + "%"));
        }

        Page<Category> page = categoryRepository.findAll(spec, pageable);
        return page.map(categoryMapper::toResponse);
    }
}
