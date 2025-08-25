package com.example.library.entity;

import com.example.library.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "role_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_group_code", nullable = false, unique = true)
    private String roleGroupCode;

    @Column(name = "role_group_name", nullable = false)
    private String roleGroupName;

    @Column(name = "description")
    private String description;

    /**
     * 1 nhóm quyền có thể gán cho nhiều người dùng
     */
    @OneToMany(mappedBy = "roleGroup", fetch = FetchType.LAZY)
    private Set<User> users;

    /**
     * 1 nhóm quyền có thể có nhiều quyền (permission)
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_group_permissions",
            joinColumns = @JoinColumn(name = "role_group_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;
}
