package com.gogetdata.company.application.dto.companyuser;

import com.gogetdata.company.domain.entity.CompanyUser;
import com.gogetdata.company.domain.entity.CompanyUserType;

public record CompanyUserResponse(Long companyUserId, Long userId , CompanyUserType type , String userName , String email) {
    public static CompanyUserResponse from(final CompanyUser companyUser) {
        return new CompanyUserResponse(
                companyUser.getCompanyUserId(),
                companyUser.getUserId(),
                companyUser.getType(),
                companyUser.getUserName(),
                companyUser.getEmail());
    }

}
