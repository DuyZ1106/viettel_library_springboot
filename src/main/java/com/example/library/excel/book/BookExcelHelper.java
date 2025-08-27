package com.example.library.excel.book;

import com.example.library.entity.Book;
import com.example.library.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookExcelHelper {

    private static final String SHEET = "Books";

    public static List<Book> excelToBooks(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Book> books = new ArrayList<>();

            if (rows.hasNext()) rows.next(); // Skip header row

            while (rows.hasNext()) {
                Row row = rows.next();
                Book book = new Book();

                book.setCode(getCellString(row.getCell(0)));
                book.setTitle(getCellString(row.getCell(1)));

                String authorsCell = getCellString(row.getCell(2));
                Set<User> authors = parseAuthorIds(authorsCell);
                book.setAuthors(authors);

                book.setPublisher(getCellString(row.getCell(3)));
                book.setPageCount(getCellInteger(row.getCell(4)));
                book.setLanguage(getCellString(row.getCell(5)));
                book.setDescription(getCellString(row.getCell(6)));
                book.setQuantity(getCellInteger(row.getCell(7)));

                books.add(book);
            }

            return books;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream booksToExcel(List<Book> books) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(SHEET);
            Row header = sheet.createRow(0);

            String[] headers = {
                    "Code", "Title", "Authors", "Publisher",
                    "Page Count", "Language", "Description", "Quantity"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIdx = 1;
            for (Book book : books) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(book.getCode());
                row.createCell(1).setCellValue(book.getTitle());

                String authorsStr = "";
                if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                    authorsStr = book.getAuthors()
                            .stream()
                            .map(u -> u.getId() != null ? u.getId().toString() : "")
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.joining(","));
                }
                row.createCell(2).setCellValue(authorsStr);

                row.createCell(3).setCellValue(book.getPublisher());
                row.createCell(4).setCellValue(book.getPageCount() != null ? book.getPageCount() : 0);
                row.createCell(5).setCellValue(book.getLanguage());
                row.createCell(6).setCellValue(book.getDescription());
                row.createCell(7).setCellValue(book.getQuantity() != null ? book.getQuantity() : 0);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Excel file: " + e.getMessage());
        }
    }

    private static String getCellString(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING); // Force to String to avoid exceptions
        return cell.getStringCellValue().trim();
    }

    private static Integer getCellInteger(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();
                return value.isEmpty() ? null : Integer.parseInt(value);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static Set<User> parseAuthorIds(String cellValue) {
        // Hỗ trợ cả dấu phẩy hoặc chấm phẩy: "1,2,3" hoặc "1; 2; 3"
        if (cellValue == null || cellValue.isBlank()) return new LinkedHashSet<>();

        String normalized = cellValue.replace(";", ",");
        String[] parts = normalized.split(",");

        Set<User> users = new LinkedHashSet<>();
        for (String raw : parts) {
            String s = raw.trim();
            if (s.isEmpty()) continue;
            try {
                Long id = Long.parseLong(s);
                User u = new User();
                u.setId(id);       // stub entity theo id — Hibernate sẽ link khi save
                users.add(u);
            } catch (NumberFormatException ignore) {
                // bỏ qua ô không hợp lệ (ví dụ chữ)
            }
        }
        return users;
    }
}
