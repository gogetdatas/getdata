package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.*;
import com.gogetdata.company.application.dto.companyuser.*;

import java.util.List;

public interface CompanyUserService {
    List<CompanyUserRegistrationResponse> registerUserToCompany(Long loginUserId,String role , List<UserRegistrationRequest> userRegistrationRequests, Long companyId);
    MessageResponse deleteCompanyUser(Long loginUserId,String role , Long userId, Long companyId);
    MessageResponse updateCompanyTypeUser(Long loginUserId,String role , Long userId, Long companyId, UpdateCompanyUserTypeRequest updateCompanyTypeRequest);
    List<CompanyUserResponse> readsCompanyUser(Long loginUserId,String role, Long companyId);
    CompanyUserResponse readCompanyUser(Long loginUserId,String role,Long companyId,Long userId);
    MessageResponse requestCompanyUser(Long loginUserId,String role,Long companyId);
    MessageResponse rejectCompanyUser(Long loginUserId,String role,Long companyId,Long companyUserId);
    List<CompanyWaitingUserResponse> readsRequestCompanyUser(Long loginUserId,String role, Long companyId);

}
