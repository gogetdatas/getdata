package com.gogetdata.company.domain.entity;

import lombok.Getter;

@Getter
public enum CompanyUserType {
    ADMIN(Authority.ADMIN),
    USER(Authority.USER),
    UNASSIGN(Authority.UNASSIGN);

    private final String authority;

    CompanyUserType(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
        public static final String UNASSIGN = "UNASSIGN";
    }
}
