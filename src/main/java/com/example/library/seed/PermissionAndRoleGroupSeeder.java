package com.example.library.seed;

import com.example.library.entity.Permission;
import com.example.library.entity.RoleGroup;
import com.example.library.repository.PermissionRepository;
import com.example.library.repository.RoleGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PermissionAndRoleGroupSeeder implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleGroupRepository roleGroupRepository;

    @Override
    public void run(String... args) {
        // Step 1: Seed permissions (if not exist)
        Map<String, String> permissions = Map.ofEntries(
                // User
                entry("ROLE_VIEW_USER", "Xem người dùng"),
                entry("ROLE_CREATE_USER", "Thêm người dùng"),
                entry("ROLE_UPDATE_USER", "Sửa người dùng"),
                entry("ROLE_DELETE_USER", "Xóa người dùng"),

                // RoleGroup
                entry("ROLE_VIEW_ROLE_GROUP", "Xem nhóm quyền"),
                entry("ROLE_CREATE_ROLE_GROUP", "Thêm nhóm quyền"),
                entry("ROLE_UPDATE_ROLE_GROUP", "Sửa nhóm quyền"),
                entry("ROLE_DELETE_ROLE_GROUP", "Xóa nhóm quyền"),

                // Permission
                entry("ROLE_VIEW_PERMISSION", "Xem quyền"),
                entry("ROLE_CREATE_PERMISSION", "Thêm quyền"),
                entry("ROLE_UPDATE_PERMISSION", "Sửa quyền"),
                entry("ROLE_DELETE_PERMISSION", "Xóa quyền"),

                // Book
                entry("ROLE_VIEW_BOOK", "Xem sách"),
                entry("ROLE_CREATE_BOOK", "Thêm sách"),
                entry("ROLE_UPDATE_BOOK", "Sửa sách"),
                entry("ROLE_DELETE_BOOK", "Xóa sách"),

                // Category
                entry("ROLE_VIEW_CATEGORY", "Xem thể loại"),
                entry("ROLE_CREATE_CATEGORY", "Thêm thể loại"),
                entry("ROLE_UPDATE_CATEGORY", "Sửa thể loại"),
                entry("ROLE_DELETE_CATEGORY", "Xóa thể loại"),

                // Borrow
                entry("ROLE_VIEW_BORROW", "Xem phiếu mượn"),
                entry("ROLE_CREATE_BORROW", "Thêm phiếu mượn"),
                entry("ROLE_UPDATE_BORROW", "Sửa phiếu mượn"),
                entry("ROLE_DELETE_BORROW", "Xóa phiếu mượn"),

                // Post
                entry("ROLE_VIEW_POST", "Xem bài viết"),
                entry("ROLE_CREATE_POST", "Thêm bài viết"),
                entry("ROLE_UPDATE_POST", "Sửa bài viết"),
                entry("ROLE_DELETE_POST", "Xóa bài viết"),

                // Comment
                entry("ROLE_VIEW_COMMENT", "Xem bình luận"),
                entry("ROLE_CREATE_COMMENT", "Thêm bình luận"),
                entry("ROLE_UPDATE_COMMENT", "Sửa bình luận"),
                entry("ROLE_DELETE_COMMENT", "Xóa bình luận")
        );

        List<Permission> savedPermissions = new ArrayList<>();
        for (var entry : permissions.entrySet()) {
            String code = entry.getKey();
            String name = entry.getValue();

            Permission p = permissionRepository.findByPermissionCode(code).orElse(null);
            if (p == null) {
                p = Permission.builder()
                        .permissionCode(code)
                        .permissionName(name)
                        .build();
                p.setIsActive(true);
                p.setIsDeleted(false);
                p.setCreatedDate(LocalDateTime.now());
                p.setCreatedBy("seeder");
                permissionRepository.save(p);
            }
            savedPermissions.add(p);
        }

        // Step 2: Seed RoleGroups and assign permissions
        seedRoleGroup("ADMIN", "Quản trị hệ thống", savedPermissions);

        seedRoleGroup("USER", "Người dùng cơ bản", savedPermissions.stream()
                .filter(p -> List.of("ROLE_VIEW_BOOK", "ROLE_VIEW_CATEGORY", "ROLE_VIEW_POST", "ROLE_CREATE_POST", "ROLE_CREATE_BORROW", "ROLE_VIEW_BORROW", "ROLE_VIEW_COMMENT", "ROLE_CREATE_COMMENT").contains(p.getPermissionCode()))
                .collect(Collectors.toList()));
    }

    private void seedRoleGroup(String code, String name, List<Permission> permissions) {
        if (roleGroupRepository.existsByRoleGroupCode(code)) return;

        RoleGroup group = RoleGroup.builder()
                .roleGroupCode(code)
                .roleGroupName(name)
                .description("Seeded by system")
                .permissions(new HashSet<>(permissions))
                .build();
        group.setIsActive(true);
        group.setIsDeleted(false);
        group.setCreatedBy("seeder");
        group.setCreatedDate(LocalDateTime.now());

        roleGroupRepository.save(group);
    }

    private static Map.Entry<String, String> entry(String key, String value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }
}