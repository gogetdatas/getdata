package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.company.CompanyResponse;
import com.gogetdata.company.application.dto.company.CreateCompanyRequest;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.company.UpdateCompanyRequest;
import com.gogetdata.company.infrastructure.filter.CustomUserDetails;

public interface CompanyService {
    CompanyResponse createCompany(CustomUserDetails customUserDetails, CreateCompanyRequest createCompanyRequest);

    CompanyResponse readCompany(CustomUserDetails customUserDetails, Long companyId);

    CompanyResponse updateCompany(CustomUserDetails customUserDetails, Long companyId, UpdateCompanyRequest updateCompanyRequest);

    MessageResponse deleteCompany(CustomUserDetails customUserDetails ,Long companyId);
}
