package com.example.library.controller.book;

import com.example.library.excel.book.BookExcelService;
import com.example.library.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/library.book")
@RequiredArgsConstructor
public class BookExcelController {

    private final BookExcelService bookExcelService;

    /**
     * Import books from Excel
     */
    @Operation(summary = "Import books from Excel file")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> importBooks(
            @Parameter(description = "Excel file to import")
            @RequestParam("file") MultipartFile file) {

        bookExcelService.importBooks(file);
        return ApiResponse.success("Import books successfully");
    }

    /**
     * Export books to Excel
     */
    @Operation(summary = "Export books to Excel file")
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportBooks(HttpServletResponse response) throws IOException {
        ByteArrayInputStream excelData = bookExcelService.exportBooks();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=books.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excelData));
    }
}
