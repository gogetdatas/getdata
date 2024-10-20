package com.gogetdata.datamanagemant.application.dto.types;

import com.gogetdata.datamanagemant.domain.entity.KeySet;

public record KeySetList(Long keySetId,String keySetName) {
    public static KeySetList from(final KeySet keySet) {
        return new KeySetList(
                keySet.getKeysetId(),
                keySet.getKeyName()
        );
    }
}
