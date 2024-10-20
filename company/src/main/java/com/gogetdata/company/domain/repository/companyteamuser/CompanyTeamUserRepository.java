package com.gogetdata.company.domain.repository.companyteamuser;

import com.gogetdata.company.domain.entity.CompanyTeamUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyTeamUserRepository extends JpaRepository<CompanyTeamUser , Long> ,CompanyTeamUserRepositoryCustom {
}
