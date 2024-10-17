package com.gogetdata.company.domain.repository.companyuser;

import com.gogetdata.company.domain.entity.Company;
import com.gogetdata.company.domain.entity.CompanyUser;

import java.util.Collection;
import java.util.List;

public interface CompanyUserRepositoryCustom {
    Company getCompanyUserAdmin(Long userId, Long companyId);
    Company getCompanyUser(Long userId,Long companyId);
    List<CompanyUser> selectWaitingForApprovalUsers(Collection<Long> companyUserId,Long companyId);
    List<CompanyUser> ApprovalUsers(Long companyId);
    CompanyUser ApprovalUser(Long companyId,Long companyUserId);
    List<CompanyUser> waitingForApprovalUsers(Long companyId);
    CompanyUser waitingForApprovalUser(Long companyId,Long companyUserId);
    CompanyUser isApprovalUser(Long companyId,Long userId);

}
