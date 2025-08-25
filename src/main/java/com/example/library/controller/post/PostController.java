package com.example.library.controller.post;

import com.example.library.dto.post.request.CreatePostRequest;
import com.example.library.dto.post.request.PostSearchRequest;
import com.example.library.dto.post.request.UpdatePostRequest;
import com.example.library.dto.post.response.PostResponse;
import com.example.library.dto.postreaction.request.PostReactionRequest;
import com.example.library.response.ApiResponse;
import com.example.library.service.post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/library/post") // <-- giống style /api/v1/library/permission
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /** Danh sách post (search + paging) */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW_POST')")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> search(
            @RequestBody PostSearchRequest request,
            Pageable pageable
    ) {
        if (request == null) request = new PostSearchRequest();
        return ResponseEntity.ok(ApiResponse.success(postService.search(request, pageable)));
    }

    /** Thêm mới post */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_POST')")
    public ResponseEntity<ApiResponse<PostResponse>> create(
            @RequestBody @Valid CreatePostRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(postService.create(request)));
    }

    /** Chi tiết post */
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_POST')")
    public ResponseEntity<ApiResponse<PostResponse>> detail(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(postService.detail(id)));
    }

    /** Cập nhật post */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE_POST')")
    public ResponseEntity<ApiResponse<PostResponse>> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdatePostRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(postService.update(id, request)));
    }

    /** Xóa mềm post */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_POST')")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /** React (LIKE/DISLIKE) */
    @PostMapping("/react")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PostResponse>> react(
            @RequestParam("id") Long id,
            @RequestBody @Valid PostReactionRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(postService.react(id, request)));
    }
}
