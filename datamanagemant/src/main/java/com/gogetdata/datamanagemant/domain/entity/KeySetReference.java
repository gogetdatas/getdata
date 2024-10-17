package com.gogetdata.datamanagemant.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "keysetreference")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class KeySetReference extends BaseEntity {
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keysetreference_id")
    private Long keySetReferenceId;

    @Column(name = "subtypekeyset_Id")
    private Long subtypeKeySetId;

    @Column(name = "keysethash")
    private String keySetHash;


    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public static KeySetReference create(Long subtypeKeySetId,String keySetHash) {
        return KeySetReference.builder()
                .subtypeKeySetId(subtypeKeySetId)
                .keySetHash(keySetHash)
                .build();
    }
}
