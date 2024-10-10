package com.gogetdata.auth.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "userinfo")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class UserInfo extends BaseEntity{
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userinfo_id")
    private Long userInfoId;

    @Column(name = "userid")
    private Long userId;

    @Column(name = "usertype")
    private String userType;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "companytype")
    private String companyType;
    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public static UserInfo create(Long userId,String userType) {
        return UserInfo.builder()
                .userId(userId)
                .userType(userType)
                .build();
    }
    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */
}
