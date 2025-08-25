package com.example.library.repository;

import com.example.library.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    // Sửa method name để khớp với tên trường trong entity
    boolean existsByCategoryNameIgnoreCase(String categoryName);

    // Có thể thêm method tìm theo categoryName
    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);

    // Hoặc dùng @Query nếu muốn custom
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE LOWER(c.categoryName) = LOWER(:name)")
    boolean existsByNameCustom(@Param("name") String name);
}