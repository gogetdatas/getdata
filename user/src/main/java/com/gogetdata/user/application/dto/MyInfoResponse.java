package com.gogetdata.user.application.dto;

import com.gogetdata.user.domain.entity.User;

public record MyInfoResponse(Long userId, String userName, String email, Long companyStatus) {
    public static MyInfoResponse from(final User user) {
        return new MyInfoResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getCompanyStatus());
    }
}
