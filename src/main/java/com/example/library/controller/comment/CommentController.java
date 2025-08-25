package com.example.library.controller.comment;

import com.example.library.dto.comment.request.CommentSearchRequest;
import com.example.library.dto.comment.request.CreateCommentRequest;
import com.example.library.dto.comment.request.UpdateCommentRequest;
import com.example.library.dto.comment.response.CommentResponse;
import com.example.library.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * Danh sách comment của 1 post cụ thể (phân trang + filter)
     * Endpoint: GET /api/v1/library.comment
     * Quyền: ROLE_VIEW_COMMENT
     */
    @GetMapping("/api/v1/library.comment")
    @PreAuthorize("hasAuthority('ROLE_VIEW_COMMENT')")
    public Page<CommentResponse> list(@RequestParam Long postId,
                                      @RequestParam(required = false) Long parentId,
                                      @RequestParam(required = false) String keyword,
                                      Pageable pageable) {
        CommentSearchRequest req = CommentSearchRequest.builder()
                .postId(postId)
                .parentId(parentId)
                .keyword(keyword)
                .build();
        return commentService.listByPost(req, pageable);
    }

    /**
     * Cây comment (tree) của 1 post
     * Endpoint: GET /api/v1/library.comment.tree
     * Quyền: ROLE_VIEW_COMMENT
     *
     * @param postId   id bài post (bắt buộc)
     * @param maxDepth giới hạn độ sâu (tuỳ chọn, null = không giới hạn)
     */
    @GetMapping("/api/v1/library.comment.tree")
    @PreAuthorize("hasAuthority('ROLE_VIEW_COMMENT')")
    public List<CommentResponse> tree(@RequestParam Long postId,
                                      @RequestParam(required = false) Integer maxDepth) {
        return commentService.tree(postId, maxDepth);
    }

    /**
     * Thêm comment vào post
     * Endpoint: POST /api/v1/library.comment.create
     * Quyền: ROLE_CREATE_COMMENT
     */
    @PostMapping("/api/v1/library.comment.create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_COMMENT')")
    public CommentResponse create(@RequestBody @Valid CreateCommentRequest request) {
        return commentService.create(request);
    }

    /**
     * Xem chi tiết comment
     * Endpoint: GET /api/v1/library.comment.detail
     * Quyền: ROLE_VIEW_COMMENT
     */
    @GetMapping("/api/v1/library.comment.detail")
    @PreAuthorize("hasAuthority('ROLE_VIEW_COMMENT')")
    public CommentResponse detail(@RequestParam Long id) {
        return commentService.detail(id);
    }

    /**
     * Cập nhật comment
     * Endpoint: PUT /api/v1/library.comment.update
     * Quyền: ROLE_UPDATE_COMMENT
     * (Kiểm tra owner thực hiện trong Service; role moderator do @PreAuthorize)
     */
    @PutMapping("/api/v1/library.comment.update")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_COMMENT')")
    public CommentResponse update(@RequestParam Long id,
                                  @RequestBody @Valid UpdateCommentRequest request) {
        return commentService.update(id, request);
    }

    /**
     * Xóa (soft) comment
     * Endpoint: DELETE /api/v1/library.comment.delete
     * Quyền: ROLE_DELETE_COMMENT
     * (Kiểm tra owner thực hiện trong Service; role moderator do @PreAuthorize)
     */
    @DeleteMapping("/api/v1/library.comment.delete")
    @PreAuthorize("hasAuthority('ROLE_DELETE_COMMENT')")
    public void delete(@RequestParam Long id) {
        commentService.delete(id);
    }
}
