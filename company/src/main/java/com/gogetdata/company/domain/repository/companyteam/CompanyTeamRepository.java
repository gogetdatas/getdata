package com.gogetdata.company.domain.repository.companyteam;

import com.gogetdata.company.domain.entity.CompanyTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyTeamRepository extends JpaRepository<CompanyTeam , Long> , CompanyTeamRepositoryCustom {
}
