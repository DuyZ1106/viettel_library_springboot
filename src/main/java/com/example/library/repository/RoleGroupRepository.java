package com.example.library.repository;

import com.example.library.entity.RoleGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleGroupRepository extends JpaRepository<RoleGroup, Long> {

    Optional<RoleGroup> findByRoleGroupCode(String code);

    boolean existsByRoleGroupCode(String code);
}
