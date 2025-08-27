package com.example.library.mapper.book;

import  com.example.library.dto.book.request.CreateBookRequest;
import com.example.library.dto.book.request.UpdateBookRequest;
import com.example.library.dto.book.response.BookResponse;
import com.example.library.entity.Book;
import com.example.library.entity.User;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "authors", source = "authors")
    BookResponse toResponse(Book book);

    List<BookResponse> toResponseList(List<Book> books);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authors", ignore = true)
    Book toEntity(CreateBookRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "authors", ignore = true)
    void updateEntity(@MappingTarget Book book, UpdateBookRequest request);

    default BookResponse.AuthorDto toAuthorDto(User user) {
        if (user == null) return null;
        BookResponse.AuthorDto dto = new BookResponse.AuthorDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        return dto;
    }

    default List<BookResponse.AuthorDto> mapAuthors(Set<User> users) {
        if (users == null) return List.of();
        return users.stream().map(this::toAuthorDto).collect(Collectors.toList());
    }
}
