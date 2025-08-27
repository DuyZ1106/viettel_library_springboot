package com.example.library.dto.book.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String code;
    private String title;
    private List<AuthorDto> authors;
    private String publisher;
    private Integer pageCount;
    private String printType;
    private String language;
    private String description;
    private Integer quantity;

    @Getter @Setter
    public static class AuthorDto {
        private Long id;
        private String fullName;
    }
}
