package com.gogetdata.datamanagemant.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "typeset")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class TypeSet extends BaseEntity{
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typeset_id")
    private Long typeSetId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "typename")
    private String typeName;


    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public static TypeSet create(Long companyId,String typeName) {
        return TypeSet.builder()
                .companyId(companyId)
                .typeName(typeName)
                .build();
    }
}
