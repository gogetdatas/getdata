package com.gogetdata.channel.domain.entity;

import lombok.Getter;

@Getter
public enum ChannelAccessType {
    ADMIN(Authority.ADMIN),
    READ(Authority.READ);

    private final String authority;

    ChannelAccessType(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String ADMIN = "ADMIN";
        public static final String READ = "READ";
    }
}
