package com.gogetdata.company.application.dto.companyuser;

import com.gogetdata.company.domain.entity.AffiliationStatus;
import com.gogetdata.company.domain.entity.CompanyUserType;

public record CompanyJoinCompanyUserDto (Long companyId , String companyName,
                                         Long companyUserId, Long userId,
                                         AffiliationStatus status, CompanyUserType type){
    public static CompanyJoinCompanyUserDto from(final Long companyId,final String companyName,
                                                 final Long companyUserId, final Long userId,
                                                 final AffiliationStatus status, CompanyUserType type) {
        return new CompanyJoinCompanyUserDto(
                companyId,
                companyName,
                companyUserId,
                userId,
                status,
                type
        );
    }
}
