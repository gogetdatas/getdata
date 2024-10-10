package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.company.CompanyResponse;
import com.gogetdata.company.application.dto.company.CreateCompanyRequest;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.company.UpdateCompanyRequest;

public interface CompanyService {
    CompanyResponse createCompany(Long userId,String role, CreateCompanyRequest createCompanyRequest);

    CompanyResponse readCompany(String role,Long loginCompanyId, Long companyId);

    CompanyResponse updateCompany(String role, Long companyId, UpdateCompanyRequest updateCompanyRequest,Long loginCompanyId, String loginCompanyType);

    MessageResponse deleteCompany(String role, Long companyId,Long loginCompanyId, String loginCompanyType);
}
