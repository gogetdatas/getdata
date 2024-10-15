package com.gogetdata.datamanagemant.application.dto.types;

import java.util.List;

public record TypeResponse(List<TypeList> typeLists) {
    public static TypeResponse from(final List<TypeList> typeLists) {
        return new TypeResponse(typeLists);
    }
}
