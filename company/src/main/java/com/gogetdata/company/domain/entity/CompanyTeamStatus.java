package com.gogetdata.company.domain.entity;

import lombok.Getter;
@Getter
public enum CompanyTeamStatus {
    PENDING(CompanyTeamStatus.Status.PENDING),
    APPROVED(CompanyTeamStatus.Status.APPROVED),
    REJECTED(CompanyTeamStatus.Status.REJECTED);

    private final String status;

    CompanyTeamStatus(String status) {
        this.status = status;
    }

    public static class Status {
        public static final String PENDING = "PENDING";
        public static final String APPROVED = "APPROVED";
        public static final String REJECTED = "REJECTED";
    }
}
