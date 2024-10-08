package com.gogetdata.company.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company_user")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class CompanyUser extends BaseEntity{
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_user_id")
    private Long companyUserId;

    @Column(name = "company_id")
    private  Long companyId;

    @Column(name = "user_id")
    private  Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private  AffiliationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private  CompanyUserType type;

    @Column(name = "username")
    private String userName;

    @Column(name = "email")
    private String email;
    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public static CompanyUser create(Long companyId , Long userId , AffiliationStatus status,CompanyUserType type,String userName , String email) {
        return CompanyUser.builder()
                .companyId(companyId)
                .userId(userId)
                .status(status)
                .type(type)
                .userName(userName)
                .email(email)
                .build();
    }

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */
    public void registerUserToCompany(AffiliationStatus status , CompanyUserType type){
        this.status = status;
        this.type = type;
    }
    public void updateTypeUserCompany(CompanyUserType type){
        this.type = type;
    }
    public void updateStatusUserCompany(AffiliationStatus status){
        this.status = status;
    }
}
