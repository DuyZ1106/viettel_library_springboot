package com.example.library.dto.book.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateBookRequest {

    @NotNull(message = "Book ID is required")
    private Long id;

    private String bookCode;

    private String title;

    private List<Long> authorIds;

    private String publisher;

    private Integer pageCount;

    private String printType;

    private String language;

    private String description;

    private Integer quantity;

    private List<Long> categoryIds;
}
