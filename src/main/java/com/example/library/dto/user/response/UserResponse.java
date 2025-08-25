package com.example.library.dto.user.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserResponse {

    private Long id;

    private String username;

    private String fullName;

    private String phoneNumber;

    private String identityNumber;

    private Integer age;

    private LocalDate birthday;

    private String address;

    private String email;
}
