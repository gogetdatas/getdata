package com.gogetdata.datamanagemant.application.dto.types;

import com.gogetdata.datamanagemant.domain.entity.KeySet;

import java.util.List;

public record KeySetMapping(List<KeySetList> keySetLists, String keySetHash) {
    public static KeySetMapping from(final List<KeySetList> keySetLists,String keySetHash) {
        return new KeySetMapping(
                keySetLists,
                keySetHash
        );
    }
}
