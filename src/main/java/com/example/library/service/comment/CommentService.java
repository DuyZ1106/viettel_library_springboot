package com.example.library.service.comment;

import com.example.library.dto.comment.request.CommentSearchRequest;
import com.example.library.dto.comment.request.CreateCommentRequest;
import com.example.library.dto.comment.request.UpdateCommentRequest;
import com.example.library.dto.comment.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    /**
     * Lấy danh sách comment (phẳng) của 1 bài post
     * Mặc định lọc isDeleted = false
     */
    Page<CommentResponse> listByPost(CommentSearchRequest request, Pageable pageable);

    /**
     * Lấy cây comment của 1 post
     * Có thể giới hạn độ sâu nếu truyền maxDepth
     */
    List<CommentResponse> tree(Long postId, Integer maxDepth);

    /**
     * Tạo comment mới (root hoặc reply)
     * Tự động cập nhật path/level và tăng comment_count của post
     */
    CommentResponse create(CreateCommentRequest request);

    /**
     * Lấy chi tiết comment
     * Không trả về nếu isDeleted = true
     */
    CommentResponse detail(Long id);

    /**
     * Cập nhật comment
     * Chỉ owner hoặc admin mới được update
     */
    CommentResponse update(Long id, UpdateCommentRequest request);

    /**
     * Xóa mềm comment
     * Chỉ owner hoặc admin mới được xóa
     */
    void delete(Long id);
}
