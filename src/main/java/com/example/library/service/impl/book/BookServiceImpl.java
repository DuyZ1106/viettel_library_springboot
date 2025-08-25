package com.example.library.service.impl.book;

import com.example.library.dto.book.request.BookSearchRequest;
import com.example.library.dto.book.request.CreateBookRequest;
import com.example.library.dto.book.request.UpdateBookRequest;
import com.example.library.dto.book.response.BookResponse;
import com.example.library.entity.Book;
import com.example.library.entity.Category;
import com.example.library.exception.BusinessException;
import com.example.library.mapper.book.BookMapper;
import com.example.library.repository.BookRepository;
import com.example.library.service.book.BookService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Join;

import static org.springframework.util.StringUtils.hasText;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponse createBook(CreateBookRequest request) {
        if (bookRepository.existsByCode(request.getCode())) {
            throw new BusinessException("error.book.code.exists");
        }
        Book book = bookMapper.toEntity(request);
        book.setIsDeleted(false);
        book.setIsActive(true);
        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public BookResponse updateBook(Long id, UpdateBookRequest request) {
        Book book = bookRepository.findById(id)
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .orElseThrow(() -> new BusinessException("error.book.not_found"));

        bookMapper.updateEntity(book, request);
        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .orElseThrow(() -> new BusinessException("error.book.not_found"));

        book.setIsDeleted(true);
        book.setIsActive(false);
        bookRepository.save(book);
    }

    @Override
    public BookResponse getBookById(Long id) {
        return bookRepository.findById(id)
                .filter(book -> !Boolean.TRUE.equals(book.getIsDeleted()))
                .map(bookMapper::toResponse)
                .orElseThrow(() -> new BusinessException("error.book.not_found"));
    }

    @Override
    public Page<BookResponse> searchBooks(BookSearchRequest request, Pageable pageable) {
        Specification<Book> spec = Specification.where(
                (root, query, cb) -> cb.isFalse(root.get("isDeleted"))
        );

        if (hasText(request.getCode())) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("code")), "%" + request.getCode().toLowerCase() + "%"));
        }

        if (hasText(request.getTitle())) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("title")), "%" + request.getTitle().toLowerCase() + "%"));
        }

        if (hasText(request.getAuthors())) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("authors")), "%" + request.getAuthors().toLowerCase() + "%"));
        }

        if (hasText(request.getPublisher())) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("publisher")), "%" + request.getPublisher().toLowerCase() + "%"));
        }

        if (request.getPageCount() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("pageCount"), request.getPageCount()));
        }

        if (hasText(request.getPrintType())) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("printType")), request.getPrintType().toLowerCase()));
        }

        if (hasText(request.getLanguage())) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("language")), request.getLanguage().toLowerCase()));
        }

        if (hasText(request.getCategoryCode())) {
            spec = spec.and((root, query, cb) -> {
                Join<Book, Category> categoryJoin = root.join("categories");
                return cb.equal(cb.lower(categoryJoin.get("categoryCode")), request.getCategoryCode().toLowerCase());
            });
        }

        Page<Book> books = bookRepository.findAll(spec, pageable);
        return books.map(bookMapper::toResponse);
    }
}
