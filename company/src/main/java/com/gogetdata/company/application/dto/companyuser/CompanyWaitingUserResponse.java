package com.gogetdata.company.application.dto.companyuser;


import com.gogetdata.company.domain.entity.CompanyUser;

public record CompanyWaitingUserResponse(Long companyUserId, Long userId,String userName, String email) {
    public static CompanyWaitingUserResponse from(final CompanyUser companyUser) {
        return new CompanyWaitingUserResponse(
                companyUser.getCompanyUserId(),
                companyUser.getUserId(),
                companyUser.getUserName(),
                companyUser.getEmail());
    }
}
