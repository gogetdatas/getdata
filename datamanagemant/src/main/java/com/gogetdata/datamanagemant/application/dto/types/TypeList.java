package com.gogetdata.datamanagemant.application.dto.types;

import com.gogetdata.datamanagemant.domain.entity.TypeSet;

public record TypeList(Long typeId, String typeName) {
    public static TypeList from(final TypeSet typeSet) {
        return new TypeList(
                typeSet.getTypeSetId(),
                typeSet.getTypeName()
        );
    }
}
