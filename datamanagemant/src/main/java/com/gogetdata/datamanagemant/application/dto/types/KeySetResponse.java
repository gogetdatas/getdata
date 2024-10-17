package com.gogetdata.datamanagemant.application.dto.types;

import java.util.List;

public record KeySetResponse(List<KeySetMapping> keySetMappings) {
    public static KeySetResponse from(final List<KeySetMapping> keySetMappings) {
        return new KeySetResponse(keySetMappings);
    }
}
