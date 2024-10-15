package com.gogetdata.channel.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class QueryRequest {
    @NotNull(message = "Type은 필수입니다.")
    private String type;
    private String subtype;
    private String hashKey;
    private String selectKeys;
    private Filters filters;
    private boolean isAggregates;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Filters {
        private Aggregates aggregates;
        private List<FilterCondition> additionalFilters; // 동적 필터 추가
        private List<AggregatesFilter> aggregatesfilter;
        private String orderBy;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Aggregates {
        private List<String> group_by;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AggregatesFilter {
        private String field;
        private String type;
        private List<CaseWhen> case_when;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CaseWhen {
        private String when;
        private String then;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FilterCondition {
        private String field;
        private String operator; // 예: eq, ne, gt, lt, gte, lte, in, nin 등
        private Object value;
    }
}
