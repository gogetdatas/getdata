package com.gogetdata.company.domain.entity;

import lombok.Getter;

@Getter
public enum CompanyTeamUserStatus {
    PENDING(CompanyTeamUserStatus.Status.PENDING),
    APPROVED(CompanyTeamUserStatus.Status.APPROVED),
    REJECTED(CompanyTeamUserStatus.Status.REJECTED);

    private final String status;

    CompanyTeamUserStatus(String status) {
        this.status = status;
    }
    public static class Status {
        public static final String PENDING = "PENDING";
        public static final String APPROVED = "APPROVED";
        public static final String REJECTED = "REJECTED";
    }
}
