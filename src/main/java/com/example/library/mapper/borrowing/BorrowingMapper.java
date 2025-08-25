package com.example.library.mapper.borrowing;

import com.example.library.dto.borrowing.request.CreateBorrowingRequest;
import com.example.library.dto.borrowing.response.BorrowingResponse;
import com.example.library.entity.Borrowing;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BorrowingMapper {

    /* ---------- request → entity ---------- */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "borrowDate",
            expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "expectedReturnDate", source = "expectedReturnDate")
    @Mapping(target = "returnDate", ignore = true)
    @Mapping(target = "user", ignore = true)   // set trong service
    @Mapping(target = "book", ignore = true)   // set trong service
    Borrowing toEntity(CreateBorrowingRequest request);

    /* ---------- entity → response ---------- */
    @Mapping(target = "bookId",    source = "book.id")
    @Mapping(target = "bookCode",  source = "book.code")
    @Mapping(target = "bookTitle", source = "book.title")
    BorrowingResponse toResponse(Borrowing entity);
}
