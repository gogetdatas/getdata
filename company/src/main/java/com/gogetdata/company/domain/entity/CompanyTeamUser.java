package com.gogetdata.company.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company_team_user")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class CompanyTeamUser extends BaseEntity {
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_team_user_id")
    private Long companyTeamUserId;

    @Column(name = "company_team_id")
    private Long companyTeamId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String userName;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_team_user_status")
    private CompanyTeamUserStatus companyTeamUserStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_team_user_type")
    private CompanyTeamUserType companyTeamUserType;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public static CompanyTeamUser create(Long companyTeamId , Long userId , String userName,String email,CompanyTeamUserStatus companyTeamUserStatus , CompanyTeamUserType companyTeamUserType) {
        return CompanyTeamUser.builder()
                .companyTeamId(companyTeamId)
                .userId(userId)
                .userName(userName)
                .email(email)
                .companyTeamUserStatus(companyTeamUserStatus)
                .companyTeamUserType(companyTeamUserType)
                .build();
    }
    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */
    public void acceptUser(CompanyTeamUserType userType){
        this.companyTeamUserStatus = CompanyTeamUserStatus.APPROVED;
        this.companyTeamUserType = userType;
    }
    public void rejectUser(){
        this.companyTeamUserStatus = CompanyTeamUserStatus.REJECTED;
    }
    public void updateUserType(CompanyTeamUserType userType){
        this.companyTeamUserType = userType;
    }
    public void deleteUser(){
        super.Delete();
    }
}
