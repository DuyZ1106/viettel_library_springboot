package com.example.library.controller.borrowing;

import com.example.library.dto.borrowing.request.BorrowingSearchRequest;
import com.example.library.dto.borrowing.request.CreateBorrowingRequest;
import com.example.library.dto.borrowing.request.UpdateBorrowingRequest;
import com.example.library.dto.borrowing.response.BorrowingResponse;
import com.example.library.response.ApiResponse;
import com.example.library.service.borrowing.BorrowingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library.borrowing")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;

    /**
     * ROLE_VIEW_BORROW - Lấy danh sách lịch sử mượn trả của chính người dùng hiện tại.
     * Ưu tiên bản ghi chưa trả (returned = false) lên đầu.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW_BORROW')")
    public ApiResponse<List<BorrowingResponse>> searchBorrowings(
            @RequestBody(required = false) BorrowingSearchRequest request
    ) {
        return ApiResponse.success(borrowingService.searchBorrowings(request));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_BORROW')")
    public ApiResponse<BorrowingResponse> createBorrowing(
            @Valid @RequestBody CreateBorrowingRequest request
    ) {
        return ApiResponse.success(borrowingService.createBorrowing(request));
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_BORROW')")
    public ApiResponse<BorrowingResponse> getBorrowingById(@PathVariable Long id) {
        return ApiResponse.success(borrowingService.getBorrowingById(id));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_BORROW')")
    public ApiResponse<BorrowingResponse> updateBorrowing(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBorrowingRequest request
    ) {
        return ApiResponse.success(borrowingService.updateBorrowing(id, request));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_BORROW')")
    public ApiResponse<Void> deleteBorrowing(@PathVariable Long id) {
        borrowingService.deleteBorrowing(id);
        return ApiResponse.success();
    }
}
