package com.example.library.service.book;

import com.example.library.dto.book.request.CreateBookRequest;
import com.example.library.dto.book.request.UpdateBookRequest;
import com.example.library.dto.book.request.BookSearchRequest;
import com.example.library.dto.book.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookResponse createBook(CreateBookRequest request);

    BookResponse updateBook(Long id, UpdateBookRequest request);

    void deleteBook(Long id);

    BookResponse getBookById(Long id);

    Page<BookResponse> searchBooks(BookSearchRequest request, Pageable pageable);
}
