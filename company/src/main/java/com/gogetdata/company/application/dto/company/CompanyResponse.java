package com.gogetdata.company.application.dto.company;

import com.gogetdata.company.domain.entity.Company;

public record CompanyResponse(Long companyId, String companyName, String companyKey) {
    public static CompanyResponse from(final Company company) {
        return new CompanyResponse(
                company.getCompanyId(),
                company.getCompanyName(),
                company.getCompanyKey());
    }
}
