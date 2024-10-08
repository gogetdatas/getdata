package com.gogetdata.company.domain.repository.companyteam;

import com.gogetdata.company.domain.entity.CompanyTeam;

import java.util.List;

public interface CompanyTeamRepositoryCustom {
    List<CompanyTeam> readRequestCompanyTeams(Long companyId);
    CompanyTeam readRequestCompanyTeam(Long companyTeamId);
}
