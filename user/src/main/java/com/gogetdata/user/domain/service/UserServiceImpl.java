package com.gogetdata.user.domain.service;

import com.gogetdata.user.application.UserService;
import com.gogetdata.user.application.dto.*;
import com.gogetdata.user.domain.entity.User;
import com.gogetdata.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public MyInfoResponse readMyInfo(Long userId, Long loginUserId,String role) {
        checkAuth(userId, loginUserId,role);
        User user = getUserIfNotDeleted(userId);
        return MyInfoResponse.from(user);
    }

    @Transactional
    @Override
    @CachePut(value = "usersById", key = "#userId")
    public MyInfoResponse updateMyInfo(Long userId, Long loginUserId,String role, UpdateMyInfoRequest updateMyInfoRequest) {
        checkAuth(userId, loginUserId,role);
        User user = getUserIfNotDeleted(userId);
        user.update(updateMyInfoRequest.getUserName());
        userRepository.save(user);
        return MyInfoResponse.from(user);
    }

    @Transactional
    @Override
    @CacheEvict(value = "usersById", key = "#userId")
    public DeleteUserResponse deleteUser(Long userId, Long loginUserId,String role) {
        checkAuth(userId, loginUserId,role);
        User user = getUserIfNotDeleted(userId);
        user.delete();
        userRepository.save(user);
        return DeleteUserResponse.from("회원탈퇴");

    }


    @Override
    @Transactional
    public List<RegistrationResults> registrationUsers(UserRegistration userRegistration) {
        List<RegistrationResults> results = new ArrayList<>();
        for (UserRegistrationRequest registrationDto : userRegistration.getUserRegistrationRequestList()) {
            try {
                User user = verify(registrationDto.getUserId());
                if (user.getCompanyId() != null) {
                    results.add(RegistrationResults.from(registrationDto.getCompanyUserId(), registrationDto.getUserId(), false, registrationDto.getType(), user.getUserName()));
                } else {
                    resister(user,userRegistration.getCompanyId(),registrationDto.getType());
                    results.add(RegistrationResults.from(registrationDto.getCompanyUserId(), registrationDto.getUserId(), true, registrationDto.getType(), user.getUserName()));
                }

            } catch (Exception e) {
                results.add(RegistrationResults.from(registrationDto.getCompanyUserId(), registrationDto.getUserId(), false, registrationDto.getType(), "Not Found User"));
            }
        }
        return results;
    }
    @Override
    @Transactional
    @CacheEvict(value = "usersById", key = "#userId")
    public Boolean deleteCompanyUser(Long userId) {
        User user = verify(userId);
        user.registrationCancel();
        userRepository.save(user);
        return true;
    }

    @Override
    public RegistrationResult registrationUser(Long userId,Long companyId) {
        User user = verify(userId);
        if(user.getCompanyId() == null){
            user.registration(companyId,"ADMIN");
            userRepository.save(user);
            return RegistrationResult.from(user,true);
        }
        return RegistrationResult.from(null,false);
    }


    @Override
    public RegistrationResult checkUser(Long userId) {
        User user = verify(userId);
        if(user.getCompanyId() == null){
            return RegistrationResult.from(user,true);
        }
        return RegistrationResult.from(null,false);
    }


    public boolean checkAuth(Long userId, Long loginUserId, String role) {
        boolean isAdmin = role.equals("ADMIN");
        if (isAdmin) {
            return true;
        }
        if (loginUserId.equals(userId)) {
            return true;
        } else {
            throw new IllegalAccessError("User ID " + userId + " does not have access rights.");
        }
    }
    @Cacheable(value = "usersById", key = "#userId")
    public User verify(Long userId) {
        return getUserIfNotDeleted(userId);
    }
    private User getUserIfNotDeleted(Long userId) {
        User user = readUser(userId);
        return validateUserNotDeleted(user);
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
    @CachePut(value = "usersById", key = "#user.userId")
    public User resister(User user,Long companyId , String companyType) {
        user.registration(companyId, companyType);
        return userRepository.save(user);
    }
}
