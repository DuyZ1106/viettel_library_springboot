package com.example.library.dto.book.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String code;
    private String title;
    private String author;
    private String publisher;
    private Integer pageCount;
    private String printType;
    private String language;
    private String description;
    private Integer quantity;
}
