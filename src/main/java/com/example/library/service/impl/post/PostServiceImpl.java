package com.example.library.service.impl.post;

import com.example.library.dto.post.request.CreatePostRequest;
import com.example.library.dto.post.request.PostSearchRequest;
import com.example.library.dto.post.request.UpdatePostRequest;
import com.example.library.dto.post.response.PostResponse;
import com.example.library.dto.postreaction.request.PostReactionRequest;
import com.example.library.entity.Book;
import com.example.library.entity.Post;
import com.example.library.entity.PostReaction;
import com.example.library.entity.User;
import com.example.library.entity.enums.ReactionType;
import com.example.library.exception.BusinessException;
import com.example.library.mapper.post.PostMapper;
import com.example.library.repository.BookRepository;
import com.example.library.repository.PostReactionRepository;
import com.example.library.repository.PostRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.post.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.springframework.util.StringUtils.hasText;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostReactionRepository postReactionRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    /* ------------------------------ helper ------------------------------ */

    private Long currentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("error.auth.user_not_found"))
                .getId();
    }

    private String generatePostCode() {
        return "POST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Post getActivePostOrThrow(Long id) {
        return postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("error.post.not_found"));
    }

    // Chỉ cho phép sort theo các field hợp lệ; tránh PropertyReferenceException (sort=string)
    private static final Set<String> ALLOWED_SORTS = Set.of(
            "createdDate", "title", "publisher", "likeCount", "dislikeCount", "commentCount", "isActive"
    );

    private Pageable sanitize(Pageable p) {
        if (p == null) return PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdDate"));
        Sort safe = Sort.unsorted();
        for (Sort.Order o : p.getSort()) {
            if (ALLOWED_SORTS.contains(o.getProperty())) {
                safe = safe.and(Sort.by(o));
            }
        }
        if (safe.isUnsorted()) {
            safe = Sort.by(Sort.Direction.DESC, "createdDate");
        }
        return PageRequest.of(p.getPageNumber(), p.getPageSize(), safe);
    }

    /**
     * Build Specification:
     * - Mặc định lọc isDeleted = false.
     * - Keyword: ép String.class trước lower(), tránh lỗi function với @Lob/Text.
     * - Dùng createdDate (khớp BaseEntity).
     */
    private Specification<Post> buildSpec(PostSearchRequest request) {
        Specification<Post> spec = Specification.where((root, query, cb) -> cb.isFalse(root.get("isDeleted")));
        if (request == null) return spec;

        if (hasText(request.getKeyword())) {
            final String kw = "%" + request.getKeyword().toLowerCase() + "%";
            spec = spec.and((root, q, cb) -> cb.or(
                    cb.like(cb.lower(root.get("title").as(String.class)), kw),
                    cb.like(cb.lower(root.get("publisher").as(String.class)), kw),
                    cb.like(cb.lower(root.get("content").as(String.class)), kw)
            ));
        }
        if (request.getAuthorId() != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("author").get("id"), request.getAuthorId()));
        }
        if (request.getBookId() != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("book").get("id"), request.getBookId()));
        }
        if (request.getIsActive() != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("isActive"), request.getIsActive()));
        }
        if (request.getCreatedFrom() != null) {
            spec = spec.and((root, q, cb) -> cb.greaterThanOrEqualTo(root.get("createdDate"), request.getCreatedFrom()));
        }
        if (request.getCreatedTo() != null) {
            spec = spec.and((root, q, cb) -> cb.lessThanOrEqualTo(root.get("createdDate"), request.getCreatedTo()));
        }
        return spec;
    }

    /* ------------------------------ business ------------------------------ */

    @Override
    public Page<PostResponse> search(PostSearchRequest request, Pageable pageable) {
        var spec = buildSpec(request);
        pageable = sanitize(pageable); // lọc sort không hợp lệ
        return postRepository.findAll(spec, pageable).map(postMapper::toResponse);
    }

    @Override
    public PostResponse create(CreatePostRequest request) {
        Long me = currentUserId();
        Post entity = postMapper.toEntity(request);

        // attach author
        User author = userRepository.findById(me)
                .orElseThrow(() -> new BusinessException("error.auth.user_not_found"));
        entity.setAuthor(author);

        // attach book
        if (request.getBookId() != null) {
            Book book = bookRepository.findById(request.getBookId())
                    .orElseThrow(() -> new BusinessException("error.book.not_found"));
            entity.setBook(book);
        }

        entity.setPostCode(generatePostCode());
        entity.setIsActive(request.getIsActive() == null ? true : request.getIsActive());
        entity.setIsDeleted(false);
        entity.setLikeCount(0L);
        entity.setDislikeCount(0L);
        entity.setCommentCount(0L);

        return postMapper.toResponse(postRepository.save(entity));
    }

    @Override
    public PostResponse detail(Long id) {
        return postMapper.toResponse(getActivePostOrThrow(id));
    }

    @Override
    public PostResponse update(Long id, UpdatePostRequest request) {
        Post post = postRepository.findByIdAndAuthorIdAndIsDeletedFalse(id, currentUserId())
                .orElseThrow(() -> new BusinessException("error.post.not_found"));

        postMapper.updateEntity(post, request);

        if (request.getBookId() != null) {
            Book book = bookRepository.findById(request.getBookId())
                    .orElseThrow(() -> new BusinessException("error.book.not_found"));
            post.setBook(book);
        }

        return postMapper.toResponse(postRepository.save(post));
    }

    @Override
    public void delete(Long id) {
        Post post = postRepository.findByIdAndAuthorIdAndIsDeletedFalse(id, currentUserId())
                .orElseThrow(() -> new BusinessException("error.post.not_found"));

        post.setIsDeleted(true);
        post.setIsActive(false);
        postRepository.save(post);
    }

    @Override
    public PostResponse react(Long id, PostReactionRequest request) {
        if (request == null || request.getType() == null) {
            throw new BusinessException("error.reaction.type_required");
        }

        Post post = getActivePostOrThrow(id);
        Long me = currentUserId();

        Optional<PostReaction> existing = postReactionRepository.findByPostIdAndUserId(id, me);

        if (existing.isEmpty()) {
            PostReaction r = PostReaction.builder()
                    .post(post)
                    .user(userRepository.findById(me)
                            .orElseThrow(() -> new BusinessException("error.auth.user_not_found")))
                    .type(request.getType())
                    .build();
            postReactionRepository.save(r);
        }
        else {
            PostReaction r = existing.get();
            if (r.getType() == request.getType()) {
                postReactionRepository.delete(r); // toggle off
            } else {
                r.setType(request.getType());     // switch type
                postReactionRepository.save(r);
            }
        }

        long like = postReactionRepository.countByPostIdAndType(id, ReactionType.LIKE);
        long dislike = postReactionRepository.countByPostIdAndType(id, ReactionType.DISLIKE);
        post.setLikeCount(like);
        post.setDislikeCount(dislike);
        postRepository.save(post);

        return postMapper.toResponse(post);
    }
}
