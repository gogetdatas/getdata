package com.gogetdata.datamanagemant.domain.repository;

import com.gogetdata.datamanagemant.domain.entity.KeySet;
import com.gogetdata.datamanagemant.domain.entity.KeySetReference;
import com.gogetdata.datamanagemant.domain.entity.SubTypeSet;
import com.gogetdata.datamanagemant.domain.entity.TypeSet;


import java.util.List;
import java.util.Optional;

public interface TypeSetRepositoryCustom {
    List<TypeSet> getTypeSet(Long companyId);
    List<SubTypeSet> getSubTypeSet(Long typeSetId);
    List<KeySetReference> getKeySetReference(Long subTypeSetId);
    List<KeySet> getKeySet(Long keySetReferenceId);
}
