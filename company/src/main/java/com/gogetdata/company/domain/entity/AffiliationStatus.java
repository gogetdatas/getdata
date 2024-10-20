package com.gogetdata.company.domain.entity;

import lombok.Getter;

@Getter
public enum AffiliationStatus {
    PENDING(Status.PENDING), // 소속 요청이 대기 중인 상태
    APPROVED(Status.APPROVED), // 소속 요청이 승인된 상태
    REJECTED(Status.REJECTED), // 소속 요청이 거부된 상태
    INVITED(Status.INVITED); // 관리자가 사용자를 회사에 초대한 상태

    private final String status;

    AffiliationStatus( String status ) {
        this.status = status;
    }
    public static class Status {
        public static final String PENDING = "PENDING";
        public static final String APPROVED = "APPROVED";
        public static final String REJECTED = "REJECTED";
        public static final String INVITED = "INVITED";
    }
}
