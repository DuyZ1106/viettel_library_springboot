package com.example.library.dto.user.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateUserRequest {

    @NotBlank
    private String fullName;

    @Pattern(regexp = "\\d{10,12}")
    private String phoneNumber;

    private String identityNumber;

    private Integer age;

    private LocalDate birthday;

    private String address;

    @Email
    private String email;
}
