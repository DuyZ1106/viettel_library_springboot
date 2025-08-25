package com.example.library.service.impl.user;

import com.example.library.dto.user.request.CreateUserRequest;
import com.example.library.dto.user.request.UpdateUserRequest;
import com.example.library.dto.user.request.UserSearchRequest;
import com.example.library.dto.user.response.UserResponse;
import com.example.library.entity.User;
import com.example.library.exception.BusinessException;
import com.example.library.mapper.user.UserMapper;
import com.example.library.repository.UserRepository;
import com.example.library.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("error.user.username.exists");
        }
        User user = userMapper.toEntity(request);
        user.setIsActive(true);
        user.setIsDeleted(false);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new BusinessException("error.user.not_found"));
        userMapper.updateEntity(user, request);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new BusinessException("error.user.not_found"));
        user.setIsDeleted(true);
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .map(userMapper::toResponse)
                .orElseThrow(() -> new BusinessException("error.user.not_found"));
    }

    @Override
    public Page<UserResponse> searchUsers(UserSearchRequest request, Pageable pageable) {
        Specification<User> spec = Specification.where(
                (root, query, cb) -> cb.isFalse(root.get("isDeleted"))
        );

        if (request != null) {
            if (hasText(request.getUsername())) {
                spec = spec.and((root, query, cb) ->
                        cb.like(cb.lower(root.get("username")), "%" + request.getUsername().toLowerCase() + "%"));
            }

            if (hasText(request.getFullName())) {
                spec = spec.and((root, query, cb) ->
                        cb.like(cb.lower(root.get("fullName")), "%" + request.getFullName().toLowerCase() + "%"));
            }

            if (hasText(request.getPhoneNumber())) {
                spec = spec.and((root, query, cb) ->
                        cb.like(cb.lower(root.get("phoneNumber")), "%" + request.getPhoneNumber().toLowerCase() + "%"));
            }

            if (hasText(request.getAddress())) {
                spec = spec.and((root, query, cb) ->
                        cb.like(cb.lower(root.get("address")), "%" + request.getAddress().toLowerCase() + "%"));
            }

            if (request.getAge() != null) {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("age"), request.getAge()));
            }

            if (request.getBirthday() != null) {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("birthday"), request.getBirthday()));
            }
        }

        Page<User> users = userRepository.findAll(spec, pageable);
        return users.map(userMapper::toResponse);
    }
}
