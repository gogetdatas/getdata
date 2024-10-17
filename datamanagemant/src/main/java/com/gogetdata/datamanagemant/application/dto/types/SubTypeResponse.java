package com.gogetdata.datamanagemant.application.dto.types;

import java.util.List;
public record SubTypeResponse(List<SubTypeList> subTypeLists) {
    public static SubTypeResponse from(final List<SubTypeList> subTypeLists) {
        return new SubTypeResponse(subTypeLists);
    }
}
