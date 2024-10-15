package com.gogetdata.datamanagemant.application.dto.data;

import lombok.*;

import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class QueryResponse {
    private Map<String, Object> data;

}
