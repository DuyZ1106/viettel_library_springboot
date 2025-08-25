package com.example.library.entity;

import com.example.library.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String identityNumber;

    private Integer age;

    private LocalDate birthday;

    private String address;

    @Column(unique = true)
    private String email;

    /**
     * Mỗi người dùng thuộc 1 nhóm quyền
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_group_id")
    private RoleGroup roleGroup;
}
