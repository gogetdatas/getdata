package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.company.CompanyResponse;
import com.gogetdata.company.application.dto.company.CreateCompanyRequest;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.company.UpdateCompanyRequest;

public interface CompanyService {
    CompanyResponse createCompany(Long userId,String role, CreateCompanyRequest createCompanyRequest);

    CompanyResponse readCompany(Long userId,String role, Long companyId);

    CompanyResponse updateCompany(Long userId,String role, Long companyId, UpdateCompanyRequest updateCompanyRequest);

    MessageResponse deleteCompany(Long userId,String role ,Long companyId);
}
