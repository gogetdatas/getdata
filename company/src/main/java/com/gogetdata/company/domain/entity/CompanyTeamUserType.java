package com.gogetdata.company.domain.entity;

import lombok.Getter;

@Getter
public enum CompanyTeamUserType {
    ADMIN(CompanyTeamUserType.Authority.ADMIN),
    USER(CompanyTeamUserType.Authority.USER),
    UNASSIGN(CompanyTeamUserType.Authority.UNASSIGN);

    private final String authority;

    CompanyTeamUserType(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
        public static final String UNASSIGN = "UNASSIGN";
    }
}
