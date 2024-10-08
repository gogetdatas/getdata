package com.gogetdata.company.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company_team")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class CompanyTeam extends BaseEntity{
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_team_id")
    private Long companyTeamId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "company_name")
    private String companyTeamName;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_team_status")
    private CompanyTeamStatus companyTeamStatus;


    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public static CompanyTeam create(Long companyId,String companyTeamName,CompanyTeamStatus companyTeamStatus){
         return CompanyTeam.builder()
                .companyId(companyId)
                .companyTeamName(companyTeamName)
                .companyTeamStatus(companyTeamStatus)
                .build();
    }

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */
    public void approveTeam(){
        this.companyTeamStatus = CompanyTeamStatus.APPROVED;
    }
    public void updateTeamName(String teamName){
        this.companyTeamName = teamName;
    }
    public void deleteTeam(){
        super.Delete();
    }
    public void rejectTeam(){
        this.companyTeamStatus = CompanyTeamStatus.REJECTED;
    }
}
