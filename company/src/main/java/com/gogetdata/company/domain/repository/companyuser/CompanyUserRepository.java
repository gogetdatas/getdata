package com.gogetdata.company.domain.repository.companyuser;

import com.gogetdata.company.domain.entity.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyUserRepository extends JpaRepository<CompanyUser , Long> , CompanyUserRepositoryCustom {

}
