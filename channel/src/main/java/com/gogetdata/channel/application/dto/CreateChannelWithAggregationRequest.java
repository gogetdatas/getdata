package com.gogetdata.channel.application.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateChannelWithAggregationRequest extends BaseCreateChannelRequest {

    private String subtype;
    private String selectKeyHash;
    private List<String> selectKeys; // ["user_id", "action"]
    private Filters filters;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Filters {
        private Aggregates aggregates;
        private List<AggregatesFilter> aggregatesFilter;
        private String orderBy; // e.g., "user_id DESC"
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Aggregates {
        private List<String> groupBy; // ["user_id"]
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AggregatesFilter {
        private String field; // "action"
        private String type;  // "COUNT"
        private List<CaseWhen> caseWhen;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CaseWhen {
        private String when; // "action = '페이지 방문'"
        private String then; // "1"
    }
}
