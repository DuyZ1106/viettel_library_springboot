package com.example.library.excel.book;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

public interface BookExcelService {
    void importBooks(MultipartFile file);
    ByteArrayInputStream exportBooks();
}
