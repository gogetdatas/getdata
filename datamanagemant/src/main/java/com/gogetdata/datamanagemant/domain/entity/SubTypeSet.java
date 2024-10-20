package com.gogetdata.datamanagemant.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subtypeset")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class SubTypeSet extends BaseEntity{
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subtypeset_id")
    private Long subTypesetId;

    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "subtypename")
    private String subtypeName;


    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public static SubTypeSet create(Long typeId, String subtypeName) {
        return SubTypeSet.builder()
                .typeId(typeId)
                .subtypeName(subtypeName)
                .build();
    }
}
