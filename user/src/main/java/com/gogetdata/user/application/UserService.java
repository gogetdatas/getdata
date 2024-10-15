package com.gogetdata.user.application;

import com.gogetdata.user.application.dto.*;

import java.util.List;

public interface UserService {

    MyInfoResponse readMyInfo(Long userId, Long loginUserId,String role);

    MyInfoResponse updateMyInfo(Long userId, Long loginUserId,String role, UpdateMyInfoRequest updateMyInfoRequest);

    DeleteUserResponse deleteUser(Long userId, Long loginUserId,String role);

    List<RegistrationResults> registrationUsers(UserRegistration userRegistration);
    RegistrationResult checkUser(Long userId);

    Boolean deleteCompanyUser(Long userId);

    RegistrationResult registrationUser(Long userId,Long companyId);
}
