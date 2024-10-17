package com.gogetdata.datamanagemant.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "keyset")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)

public class KeySet extends BaseEntity{
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyset_id")
    private Long keysetId;

    @Column(name = "keysetreference_id")
    private Long keySetReferenceId;

    @Column(name = "keyname")
    private String keyName;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public static KeySet create(Long keySetReferenceId ,String keyName) {
        return KeySet.builder()
                .keySetReferenceId(keySetReferenceId)
                .keyName(keyName)
                .build();
    }
}
