package com.gogetdata.user.domain.service;

import com.gogetdata.user.application.UserService;
import com.gogetdata.user.application.dto.DeleteUserResponse;
import com.gogetdata.user.application.dto.MyInfoResponse;
import com.gogetdata.user.application.dto.UpdateMyInfoRequest;
import com.gogetdata.user.domain.entity.User;
import com.gogetdata.user.domain.repository.UserRepository;
import com.gogetdata.user.infrastructure.filter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public MyInfoResponse readMyInfo(Long userId , CustomUserDetails userDetails) {
        checkAuth(userId,userDetails);
        User user = validateUserNotDeleted(readUser(userId));
        return MyInfoResponse.from(user);
    }

    @Transactional
    @Override
    public MyInfoResponse updateMyInfo(Long userId, CustomUserDetails userDetails, UpdateMyInfoRequest updateMyInfoRequest) {
        checkAuth(userId,userDetails);
        User user = validateUserNotDeleted(readUser(userId));
        user.update(updateMyInfoRequest.getUserName());
        userRepository.save(user);
        return MyInfoResponse.from(user);
    }
    @Transactional
    @Override
    public DeleteUserResponse deleteUser(Long userId, CustomUserDetails userDetails) {
        checkAuth(userId,userDetails);
        User user = validateUserNotDeleted(readUser(userId));
        user.delete();
        userRepository.save(user);
        return DeleteUserResponse.from("회원탈퇴");

    }
    public boolean checkAuth(Long userId,CustomUserDetails userDetails) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
        if (isAdmin) {
            return true;
        }
        if (userDetails.getUserId().equals(userId)) {
            return true;
        } else {
            throw new AccessDeniedException("User ID " + userId + " does not have access rights.");
        }
    }

    public User readUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("No user found with id " + userId));
    }

    public User validateUserNotDeleted(User user) {
        if (user.isDeleted()) {
            throw new NoSuchElementException("User with id " + user.getUserId() + " is deleted.");
        }
        return user;
    }

}
