package com.example.library.repository;

import com.example.library.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    boolean existsByPermissionCode(String permissionCode);

    Optional<Permission> findByPermissionCode(String permissionCode);
}
