package com.gogetdata.user.application;

import com.gogetdata.user.application.dto.*;
import com.gogetdata.user.infrastructure.filter.CustomUserDetails;

import java.util.List;

public interface UserService {

    MyInfoResponse readMyInfo(Long userId, CustomUserDetails userDetails);

    MyInfoResponse updateMyInfo(Long userId, CustomUserDetails userDetails, UpdateMyInfoRequest updateMyInfoRequest);

    DeleteUserResponse deleteUser(Long userId, CustomUserDetails customUserDetails);

    List<RegistrationResults> registrationUsers(List<UserRegistrationDto> userRegistrationDto);
    Boolean checkUser(Long userId);

    Boolean deleteCompanyUser(Long userId);

    RegistrationResult registrationUser(Long userId);
}
