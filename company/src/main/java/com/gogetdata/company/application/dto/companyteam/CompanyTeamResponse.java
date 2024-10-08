package com.gogetdata.company.application.dto.companyteam;

import com.gogetdata.company.domain.entity.CompanyTeam;

public record CompanyTeamResponse (Long companyTeamId, String companyTeamName){
    public static CompanyTeamResponse from(final CompanyTeam companyTeam) {
        return new CompanyTeamResponse(
                companyTeam.getCompanyTeamId(),
                companyTeam.getCompanyTeamName());
    }
}
