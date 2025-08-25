package com.example.library.dto.borrowing.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingResponse {

    private Long        id;

    /* thông tin sách */
    private Long        bookId;
    private String      bookCode;
    private String      bookTitle;

    /* mốc thời gian */
    private LocalDate   borrowDate;
    private LocalDate   expectedReturnDate;
    private LocalDate   returnDate;
}
