package com.gogetdata.datamanagemant.application.dto.types;

import com.gogetdata.datamanagemant.domain.entity.SubTypeSet;

public record SubTypeList(Long typeId, String typeName) {
    public static SubTypeList from(final SubTypeSet subTypeSet) {
        return new SubTypeList(
                subTypeSet.getSubTypesetId(),
                subTypeSet.getSubtypeName()
        );
    }
}