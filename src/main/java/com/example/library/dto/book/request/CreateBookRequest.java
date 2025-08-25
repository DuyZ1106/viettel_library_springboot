package com.example.library.dto.book.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String title;

    private String authors;

    private String publisher;

    private Integer pageCount;

    private String printType;

    private String language;

    private String description;

    private Integer quantity;
}
