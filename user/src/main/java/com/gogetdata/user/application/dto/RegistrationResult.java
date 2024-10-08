package com.gogetdata.user.application.dto;

import com.gogetdata.user.domain.entity.User;

public record RegistrationResult(Long userId  , String userName, String email,boolean isSuccess){
    public static RegistrationResult from(final User user ,boolean isSuccess) {
        return new RegistrationResult(user.getUserId(), user.getUserName(), user.getEmail(), isSuccess);
    }
}
