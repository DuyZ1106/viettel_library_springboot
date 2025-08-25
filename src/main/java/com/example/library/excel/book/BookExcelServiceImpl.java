package com.example.library.excel.book;

import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookExcelServiceImpl implements BookExcelService {

    private final BookRepository bookRepository;

    @Override
    public void importBooks(MultipartFile file) {
        List<Book> books = BookExcelHelper.excelToBooks(file);
        bookRepository.saveAll(books);
    }

    @Override
    public ByteArrayInputStream exportBooks() {
        List<Book> books = bookRepository.findAll();
        return BookExcelHelper.booksToExcel(books);
    }
}
