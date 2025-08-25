package com.example.library.repository;

import com.example.library.entity.Borrowing;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowingRepository
        extends JpaRepository<Borrowing, Long>,
        JpaSpecificationExecutor<Borrowing> {

    /** Số bản ghi đang mượn (returnDate null) của một quyển sách */
    @Query("""
           SELECT COUNT(b)
           FROM Borrowing b
           WHERE b.book.id = :bookId
             AND b.returnDate IS NULL
           """)
    long countActiveByBook(@Param("bookId") Long bookId);

    /** Kiểm tra user đã mượn quyển này mà chưa trả chưa */
    boolean existsByUserIdAndBookIdAndReturnDateIsNull(Long userId, Long bookId);

    /** Tìm bản ghi theo id + user (đảm bảo chỉ truy cập bản của chính mình) */
    Optional<Borrowing> findByIdAndUserId(Long id, Long userId);
}
