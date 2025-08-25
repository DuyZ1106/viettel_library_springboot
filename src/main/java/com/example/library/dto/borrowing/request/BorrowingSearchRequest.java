package com.example.library.dto.borrowing.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingSearchRequest {

    private Boolean returned;
}
