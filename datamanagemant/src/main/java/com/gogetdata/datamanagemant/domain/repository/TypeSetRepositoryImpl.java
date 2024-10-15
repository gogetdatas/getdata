package com.gogetdata.datamanagemant.domain.repository;

import com.gogetdata.datamanagemant.domain.entity.KeySet;
import com.gogetdata.datamanagemant.domain.entity.KeySetReference;
import com.gogetdata.datamanagemant.domain.entity.SubTypeSet;
import com.gogetdata.datamanagemant.domain.entity.TypeSet;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.gogetdata.datamanagemant.domain.entity.QTypeSet.typeSet;
import static com.gogetdata.datamanagemant.domain.entity.QSubTypeSet.subTypeSet;
import static com.gogetdata.datamanagemant.domain.entity.QKeySetReference.keySetReference;
import static com.gogetdata.datamanagemant.domain.entity.QKeySet.keySet;

@Service
@RequiredArgsConstructor
public class TypeSetRepositoryImpl implements TypeSetRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public List<TypeSet> getTypeSet(Long companyId) {
        return queryFactory.selectFrom(typeSet)
                .where(typeSet.companyId.eq(companyId)
                        .and(typeSet.isDeleted.eq(false))
                )
                .fetch();
    }
    @Override
    public List<SubTypeSet> getSubTypeSet(Long typeSetId) {
        return queryFactory.selectFrom(subTypeSet)
                .where(subTypeSet.typeId.eq(typeSetId)
                        .and(subTypeSet.isDeleted.eq(false))
                )
                .fetch();
    }

    @Override
    public List<KeySetReference> getKeySetReference(Long subTypeSetId) {
        return queryFactory.selectFrom(keySetReference)
                .where(keySetReference.subtypeKeySetId.eq(subTypeSetId)
                        .and(keySetReference.isDeleted.eq(false))
                )
                .fetch();
    }

    @Override
    public List<KeySet> getKeySet(Long keySetReferenceId) {
        return queryFactory.selectFrom(keySet)
                .innerJoin(keySet).on(keySet.keySetReferenceId.eq(keySetReference.keySetReferenceId))
                .where(keySet.keySetReferenceId.eq(keySetReferenceId)
                        .and(keySetReference.isDeleted.eq(false))
                        .and(keySet.isDeleted.eq(false))
                )
                .fetch();
    }
}
