package com.gogetdata.auth.domain.entity;

import lombok.Getter;


@Getter
public enum UserTypeEnum {
    ADMIN(Authority.ADMIN),
    USER(Authority.USER);

    private final String authority;

    UserTypeEnum(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USER = "ROLE_USER";
    }
}
