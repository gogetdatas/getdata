package com.gogetdata.user.application;

import com.gogetdata.user.application.dto.DeleteUserResponse;
import com.gogetdata.user.application.dto.MyInfoResponse;
import com.gogetdata.user.application.dto.UpdateMyInfoRequest;
import com.gogetdata.user.infrastructure.filter.CustomUserDetails;

public interface UserService {

    MyInfoResponse readMyInfo(Long userId, CustomUserDetails userDetails);

    MyInfoResponse updateMyInfo(Long userId, CustomUserDetails userDetails, UpdateMyInfoRequest updateMyInfoRequest);

    DeleteUserResponse deleteUser(Long userId, CustomUserDetails customUserDetails);
}
