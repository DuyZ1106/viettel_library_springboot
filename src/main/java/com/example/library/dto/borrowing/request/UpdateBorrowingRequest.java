package com.example.library.dto.borrowing.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Update bản ghi mượn – thường dùng để “trả sách”.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBorrowingRequest {

    /** Ngày trả (≤ hôm nay, ≥ ngày mượn) */
    @NotNull
    @PastOrPresent
    private LocalDate returnDate;
}
