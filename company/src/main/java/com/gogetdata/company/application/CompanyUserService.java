package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.*;
import com.gogetdata.company.application.dto.companyuser.*;

import java.util.List;

public interface CompanyUserService {
    List<CompanyUserRegistrationResponse> registerUserToCompany(
            String role,
            List<UserRegistrationRequest> userRegistrationRequests,
            Long companyId,
            Long loginCompanyId,
            String loginCompanyType
    );
    MessageResponse deleteCompanyUser(
            Long loginUserId,
            String role,
            Long companyUserId,
            Long companyId,
            Long loginCompanyId,
            String loginCompanyType
    );
    MessageResponse updateCompanyTypeUser(
            String role,
            Long companyUserId,
            Long companyId,
            Long loginCompanyId,
            String loginCompanyType,
            UpdateCompanyUserTypeRequest updateCompanyUserTypeRequest
    );
    List<CompanyUserResponse> readsCompanyUser(
            String role,
            Long companyId,
            Long loginCompanyId
    );
    CompanyUserResponse readCompanyUser(
            String role,
            Long companyId,
            Long companyUserId,
            Long loginCompanyId
    );
    MessageResponse requestCompanyUser(
            Long loginUserId,
            String role,
            Long companyId
    );
    MessageResponse rejectCompanyUser(
            String role,
            Long companyId,
            Long companyUserId,
            Long loginCompanyId,
            String loginCompanyType
    );
    List<CompanyWaitingUserResponse> readsRequestCompanyUser(
            String role,
            Long companyId,
            Long loginCompanyId,
            String loginCompanyType
    );

}
