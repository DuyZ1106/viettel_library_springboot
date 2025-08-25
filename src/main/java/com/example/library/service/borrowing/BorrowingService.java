package com.example.library.service.borrowing;

import com.example.library.dto.borrowing.request.*;
import com.example.library.dto.borrowing.response.BorrowingResponse;

import java.util.List;

public interface BorrowingService {

    BorrowingResponse createBorrowing(CreateBorrowingRequest request);

    BorrowingResponse updateBorrowing(Long id, UpdateBorrowingRequest request);

    void deleteBorrowing(Long id);

    BorrowingResponse getBorrowingById(Long id);

    List<BorrowingResponse> searchBorrowings(BorrowingSearchRequest request);
}
