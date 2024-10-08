package com.gogetdata.company.application.dto.companyuser;

public record CompanyUserRegistrationResponse(String userName) {
    public static CompanyUserRegistrationResponse from(final String  userName) {
        return new CompanyUserRegistrationResponse(
                userName);
    }

}
