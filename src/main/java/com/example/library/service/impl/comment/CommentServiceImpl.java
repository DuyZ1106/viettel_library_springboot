package com.example.library.service.impl.comment;

import com.example.library.dto.comment.request.CommentSearchRequest;
import com.example.library.dto.comment.request.CreateCommentRequest;
import com.example.library.dto.comment.request.UpdateCommentRequest;
import com.example.library.dto.comment.response.CommentResponse;
import com.example.library.entity.Comment;
import com.example.library.entity.Post;
import com.example.library.entity.User;
import com.example.library.exception.BusinessException;
import com.example.library.mapper.comment.CommentMapper;
import com.example.library.repository.CommentRepository;
import com.example.library.repository.PostRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.comment.CommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    /* ---------------------------------- helper ---------------------------------- */

    private Long currentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("error.auth.user_not_found"))
                .getId();
    }

    private static String padId(Long id) {
        return String.format("%010d", id);
    }

    private Post getActivePostOrThrow(Long id) {
        return postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("error.post.not_found"));
    }

    private Comment getActiveCommentOrThrow(Long id) {
        return commentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("error.comment.not_found"));
    }

    private void syncPostCommentCount(Long postId) {
        long cnt = commentRepository.countByPostIdAndIsDeletedFalse(postId);
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new BusinessException("error.post.not_found"));
        post.setCommentCount(cnt);
        postRepository.save(post);
    }

    /* ---------------------------------- list ---------------------------------- */
    @Override
    public Page<CommentResponse> listByPost(CommentSearchRequest request, Pageable pageable) {
        Specification<Comment> spec = Specification.where((root, query, cb) -> cb.isFalse(root.get("isDeleted")));

        if (request != null) {
            if (request.getPostId() != null) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("post").get("id"), request.getPostId()));
            }
            if (request.getParentId() != null) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("parent").get("id"), request.getParentId()));
            }
            if (hasText(request.getKeyword())) {
                String kw = "%" + request.getKeyword().toLowerCase() + "%";
                spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("content")), kw));
            }
        }

        Sort sort = pageable.getSort().isUnsorted()
                ? Sort.by(Sort.Order.asc("path"))
                : pageable.getSort();

        Page<Comment> page = commentRepository.findAll(
                spec, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort)
        );
        return page.map(commentMapper::toResponse);
    }

    /* ---------------------------------- tree ---------------------------------- */
    @Override
    public List<CommentResponse> tree(Long postId, Integer maxDepth) {
        Post post = getActivePostOrThrow(postId);

        List<Comment> comments = commentRepository.findByPostIdAndIsDeletedFalseOrderByPathAsc(post.getId());

        if (maxDepth != null) {
            comments = comments.stream()
                    .filter(c -> c.getLevel() != null && c.getLevel() <= maxDepth)
                    .toList();
        }

        Map<Long, CommentResponse> map = new LinkedHashMap<>();
        List<CommentResponse> roots = new ArrayList<>();

        for (Comment c : comments) {
            CommentResponse dto = commentMapper.toResponse(c);
            map.put(c.getId(), dto);

            if (c.getParent() == null) {
                roots.add(dto);
            } else {
                CommentResponse parentDto = map.get(c.getParent().getId());
                if (parentDto != null) {
                    if (parentDto.getChildren() == null) parentDto.setChildren(new ArrayList<>());
                    parentDto.getChildren().add(dto);
                } else {
                    roots.add(dto); // fallback nếu parent không có trong batch
                }
            }
        }
        return roots;
    }

    /* ---------------------------------- create ---------------------------------- */
    @Override
    public CommentResponse create(CreateCommentRequest request) {
        if (request.getPostId() == null) throw new BusinessException("error.comment.post_required");
        if (!hasText(request.getContent())) throw new BusinessException("error.comment.content_required");

        Post post = getActivePostOrThrow(request.getPostId());

        User author = userRepository.findById(currentUserId())
                .orElseThrow(() -> new BusinessException("error.auth.user_not_found"));

        Comment parent = null;
        if (request.getParentId() != null) {
            parent = getActiveCommentOrThrow(request.getParentId());
            if (!Objects.equals(parent.getPost().getId(), post.getId())) {
                throw new BusinessException("error.comment.parent_wrong_post");
            }
        }

        // Lưu lần 1 để có ID
        Comment entity = Comment.builder()
                .post(post)
                .author(author)
                .parent(parent)
                .content(request.getContent())
                .path("TEMP")
                .level(parent == null ? 0 : (parent.getLevel() == null ? 0 : parent.getLevel() + 1))
                .build();
        entity.setIsDeleted(false);
        entity.setIsActive(request.getIsActive() == null || request.getIsActive());

        entity = commentRepository.save(entity);

        // Cập nhật path theo ID
        String self = padId(entity.getId());
        entity.setPath(parent == null ? self : parent.getPath() + "/" + self);
        entity = commentRepository.save(entity);

        // Đồng bộ counter
        syncPostCommentCount(post.getId());
        return commentMapper.toResponse(entity);
    }

    /* ---------------------------------- detail ---------------------------------- */
    @Override
    public CommentResponse detail(Long id) {
        return commentMapper.toResponse(getActiveCommentOrThrow(id));
    }

    /* ---------------------------------- update ---------------------------------- */
    @Override
    public CommentResponse update(Long id, UpdateCommentRequest request) {
        // Owner-only (controller lo quyền moderator)
        Comment entity = commentRepository.findByIdAndAuthorIdAndIsDeletedFalse(id, currentUserId())
                .orElseThrow(() -> new BusinessException("error.comment.not_found"));

        if (hasText(request.getContent())) {
            entity.setContent(request.getContent());
        }
        if (request.getIsActive() != null) {
            entity.setIsActive(request.getIsActive());
        }
        return commentMapper.toResponse(commentRepository.save(entity));
    }

    /* ---------------------------------- delete ---------------------------------- */
    @Override
    public void delete(Long id) {
        // Owner-only (controller lo quyền moderator)
        Comment entity = commentRepository.findByIdAndAuthorIdAndIsDeletedFalse(id, currentUserId())
                .orElseThrow(() -> new BusinessException("error.comment.not_found"));

        entity.setIsDeleted(true);
        entity.setIsActive(false);
        commentRepository.save(entity);

        syncPostCommentCount(entity.getPost().getId());
    }
}
