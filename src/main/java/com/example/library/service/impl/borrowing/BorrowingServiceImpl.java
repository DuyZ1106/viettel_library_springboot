package com.example.library.service.impl.borrowing;

import com.example.library.dto.borrowing.request.*;
import com.example.library.dto.borrowing.response.BorrowingResponse;
import com.example.library.entity.*;
import com.example.library.exception.BusinessException;
import com.example.library.mapper.borrowing.BorrowingMapper;
import com.example.library.repository.*;
import com.example.library.service.borrowing.BorrowingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@Transactional
@RequiredArgsConstructor
public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowingMapper borrowingMapper;

    /* ---------------------------------- helper ---------------------------------- */
    private Long currentUserId() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("error.user.not_found"))
                .getId();
    }

    /* ---------------------------------- create ---------------------------------- */
    @Override
    public BorrowingResponse createBorrowing(CreateBorrowingRequest request) {
        Long userId = currentUserId();

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new BusinessException("error.book.not_found"));

        long active = borrowingRepository.countActiveByBook(book.getId());
        if (active >= book.getQuantity()) {
            throw new BusinessException("error.book.out_of_stock");
        }

        if (borrowingRepository.existsByUserIdAndBookIdAndReturnDateIsNull(userId, book.getId())) {
            throw new BusinessException("error.book.already_borrowed");
        }

        Borrowing entity = borrowingMapper.toEntity(request);
        entity.setBook(book);
        entity.setUser(userRepository.getReferenceById(userId));
        entity.setBorrowDate(LocalDate.now());
        entity.setIsActive(true);
        entity.setIsDeleted(false);

        return borrowingMapper.toResponse(borrowingRepository.save(entity));
    }

    /* ---------------------------------- update ---------------------------------- */
    @Override
    public BorrowingResponse updateBorrowing(Long id, UpdateBorrowingRequest request) {
        Borrowing entity = borrowingRepository.findByIdAndUserId(id, currentUserId())
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .orElseThrow(() -> new BusinessException("error.borrowing.not_found"));

        if (entity.getReturnDate() != null) {
            throw new BusinessException("error.borrowing.already_returned");
        }
        if (request.getReturnDate().isBefore(entity.getBorrowDate())) {
            throw new BusinessException("error.return_date.invalid");
        }

        entity.setReturnDate(request.getReturnDate());
        return borrowingMapper.toResponse(borrowingRepository.save(entity));
    }

    /* ---------------------------------- delete ---------------------------------- */
    @Override
    public void deleteBorrowing(Long id) {
        Borrowing entity = borrowingRepository.findByIdAndUserId(id, currentUserId())
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .orElseThrow(() -> new BusinessException("error.borrowing.not_found"));

        entity.setIsDeleted(true);
        entity.setIsActive(false);
        borrowingRepository.save(entity);
    }

    /* ---------------------------------- detail ---------------------------------- */
    @Override
    public BorrowingResponse getBorrowingById(Long id) {
        return borrowingRepository.findByIdAndUserId(id, currentUserId())
                .filter(b -> !Boolean.TRUE.equals(b.getIsDeleted()))
                .map(borrowingMapper::toResponse)
                .orElseThrow(() -> new BusinessException("error.borrowing.not_found"));
    }

    /* ---------------------------------- search ---------------------------------- */
    @Override
    public List<BorrowingResponse> searchBorrowings(BorrowingSearchRequest request) {
        Specification<Borrowing> spec = Specification.<Borrowing>where(
                (root, query, cb) -> cb.isFalse(root.get("isDeleted"))
        ).and(
                (root, query, cb) -> cb.equal(root.get("user").get("id"), currentUserId())
        );

        if (request != null && request.getReturned() != null) {
            if (request.getReturned()) {
                spec = spec.and((root, query, cb) -> cb.isNotNull(root.get("returnDate")));
            } else {
                spec = spec.and((root, query, cb) -> cb.isNull(root.get("returnDate")));
            }
        }

        Sort sort = Sort.by(
                Sort.Order.asc("returnDate").nullsFirst(),
                Sort.Order.desc("borrowDate")
        );

        List<Borrowing> result = borrowingRepository.findAll(spec, sort);
        return result.stream().map(borrowingMapper::toResponse).toList();
    }
}
