package com.gogetdata.company.application.dto.companyteam;

import com.gogetdata.company.domain.entity.CompanyTeam;
import com.gogetdata.company.domain.entity.CompanyTeamStatus;

public record RequestCompanyTeamResponse(Long companyTeamId, String companyTeamName,CompanyTeamStatus status) {
    public static RequestCompanyTeamResponse from(final CompanyTeam companyTeam) {
        return new RequestCompanyTeamResponse(
                companyTeam.getCompanyTeamId(),
                companyTeam.getCompanyTeamName(),
                companyTeam.getCompanyTeamStatus());
    }

}
