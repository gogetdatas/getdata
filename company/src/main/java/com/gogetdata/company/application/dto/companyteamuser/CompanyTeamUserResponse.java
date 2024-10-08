package com.gogetdata.company.application.dto.companyteamuser;

import com.gogetdata.company.domain.entity.CompanyTeamUser;
import com.gogetdata.company.domain.entity.CompanyTeamUserType;

public record CompanyTeamUserResponse(Long companyTeamUserId, String userName, String email, CompanyTeamUserType userType) {
    public static CompanyTeamUserResponse from(final CompanyTeamUser companyTeamUser) {
        return new CompanyTeamUserResponse(
                companyTeamUser.getCompanyTeamUserId(),
                companyTeamUser.getUserName(),
                companyTeamUser.getEmail(),
                companyTeamUser.getCompanyTeamUserType()
        );
    }
}
