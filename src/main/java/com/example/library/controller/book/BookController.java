package com.example.library.controller.book;

import com.example.library.dto.book.request.CreateBookRequest;
import com.example.library.dto.book.request.UpdateBookRequest;
import com.example.library.dto.book.request.BookSearchRequest;
import com.example.library.dto.book.response.BookResponse;
import com.example.library.response.ApiResponse;
import com.example.library.service.book.BookService;
import com.example.library.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/library/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * ROLE_VIEW_BOOK - Tìm kiếm danh sách sách (phân trang + tìm kiếm nhiều điều kiện)
     * Dùng POST để gửi JSON request body.
     */
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_VIEW_BOOK')")
    public ApiResponse<Page<BookResponse>> searchBooks(
            @RequestBody(required = false) BookSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (request == null) {
            request = new BookSearchRequest(); // fallback nếu không gửi body
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return ResponseUtil.success(bookService.searchBooks(request, pageable)).getBody();
    }

    /**
     * ROLE_CREATE_BOOK - Thêm sách mới
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_CREATE_BOOK')")
    public ApiResponse<BookResponse> createBook(@Valid @RequestBody CreateBookRequest request) {
        return ResponseUtil.success(bookService.createBook(request)).getBody();
    }

    /**
     * ROLE_VIEW_BOOK - Xem chi tiết sách
     */
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasRole('ROLE_VIEW_BOOK')")
    public ApiResponse<BookResponse> getBook(@PathVariable Long id) {
        return ResponseUtil.success(bookService.getBookById(id)).getBody();
    }

    /**
     * ROLE_UPDATE_BOOK - Cập nhật sách
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_UPDATE_BOOK')")
    public ApiResponse<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookRequest request) {
        return ResponseUtil.success(bookService.updateBook(id, request)).getBody();
    }

    /**
     * ROLE_DELETE_BOOK - Xóa sách (xóa mềm)
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_DELETE_BOOK')")
    public ApiResponse<?> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseUtil.success(null).getBody();
    }
}
