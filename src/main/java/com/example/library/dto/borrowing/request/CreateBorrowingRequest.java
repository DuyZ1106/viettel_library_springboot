package com.example.library.dto.borrowing.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBorrowingRequest {

    /** ID sách muốn mượn */
    @NotNull
    private Long bookId;

    /** Hạn trả (≥ hôm nay) */
    @NotNull
    @FutureOrPresent
    private LocalDate expectedReturnDate;
}
