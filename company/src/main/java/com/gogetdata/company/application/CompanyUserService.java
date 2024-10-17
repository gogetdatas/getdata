package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.*;
import com.gogetdata.company.application.dto.companyuser.*;
import com.gogetdata.company.infrastructure.filter.CustomUserDetails;

import java.util.List;

public interface CompanyUserService {
    List<CompanyUserRegistrationResponse> registerUserToCompany(CustomUserDetails customUserDetails, List<UserRegistrationRequest> userRegistrationRequests, Long companyId);
    MessageResponse deleteCompanyUser(CustomUserDetails customUserDetails , Long userId, Long companyId);
    MessageResponse updateCompanyTypeUser(CustomUserDetails customUserDetails , Long userId, Long companyId, UpdateCompanyUserTypeRequest updateCompanyTypeRequest);
    List<CompanyUserResponse> readsCompanyUser(CustomUserDetails customUserDetails, Long companyId);
    CompanyUserResponse readCompanyUser(CustomUserDetails customUserDetails,Long companyId,Long userId);
    MessageResponse requestCompanyUser(CustomUserDetails customUserDetails,Long companyId);
    MessageResponse rejectCompanyUser(CustomUserDetails customUserDetails,Long companyId,Long companyUserId);
    List<CompanyWaitingUserResponse> readsRequestCompanyUser(CustomUserDetails customUserDetails, Long companyId);
    List<CompanyUserResponse> searchCompanyUser(CustomUserDetails customUserDetails , Long companyId,String companyUserName);
}
