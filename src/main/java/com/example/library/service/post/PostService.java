package com.example.library.service.post;

import com.example.library.dto.post.request.CreatePostRequest;
import com.example.library.dto.post.request.PostSearchRequest;
import com.example.library.dto.post.request.UpdatePostRequest;
import com.example.library.dto.post.response.PostResponse;
import com.example.library.dto.postreaction.request.PostReactionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    /**
     * Danh sách post theo điều kiện tìm kiếm; mặc định lọc isDeleted = false.
     */
    Page<PostResponse> search(PostSearchRequest request, Pageable pageable);

    /**
     * Tạo mới post (title, content bắt buộc; publisher tuỳ chọn; bookId tuỳ chọn).
     */
    PostResponse create(CreatePostRequest request);

    /**
     * Xem chi tiết post; nếu đã soft-delete thì ném BusinessException.
     */
    PostResponse detail(Long id);

    /**
     * Chỉ tác giả hoặc admin mới được update.
     */
    PostResponse update(Long id, UpdatePostRequest request);

    /**
     * Soft-delete: set isDeleted = true; ẩn khỏi list/detail.
     */
    void delete(Long id);

    /**
     * React bài viết: LIKE / DISLIKE, hành vi toggle/đổi loại.
     * Bất kỳ user đăng nhập nào cũng được react.
     * Trả về PostResponse để cập nhật likeCount/dislikeCount mới.
     */
    PostResponse react(Long id, PostReactionRequest request);
}
