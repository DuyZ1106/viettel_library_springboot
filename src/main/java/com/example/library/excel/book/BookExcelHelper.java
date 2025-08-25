package com.example.library.excel.book;

import com.example.library.entity.Book;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
                book.setAuthors(getCellString(row.getCell(2)));
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
                row.createCell(2).setCellValue(book.getAuthors());
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
}
