package com.example.library.dto.book.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSearchRequest {
    private String code;
    private String title;
    private String authors;
    private String publisher;
    private Integer pageCount;
    private String printType;
    private String language;
    private String categoryCode;
}
