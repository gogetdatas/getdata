package com.gogetdata.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class User extends BaseEntity{
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "usertype")
    @Enumerated(EnumType.STRING)
    private UserTypeEnum userType;

    @Column(name = "company_status")
    private Long companyStatus;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public static User create(String userName,String email, String password,UserTypeEnum usertype) {
        return User.builder()
                .userName(userName)
                .email(email)
                .password(password)
                .userType(usertype)
                .companyStatus(0L)
                .build();
    }
    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */
    public void update(String userName){
        this.userName = userName;
    }
    public void delete(){
        super.Delete();
    }
}
