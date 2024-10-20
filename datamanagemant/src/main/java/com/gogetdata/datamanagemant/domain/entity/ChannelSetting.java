package com.gogetdata.datamanagemant.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "channel_settings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelSetting {

    @Id
    private String id; // MongoDB의 기본 ObjectId
    private Long channelId;
    private Long companyId;
    private String type; // 예: "사용자 활동"
    private boolean isAggregates;
    private String subtype; // 예: "클릭"

    private String selectKeyHash; // 예: "user_id"

    private List<String> selectKeys; // 예: ["user_id", "action"]

    private Filters filters; // 집계 설정

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder

    public static class Filters {
        private Aggregates aggregates;
        private List<AggregatesFilter> aggregatesFilter;
        private String orderBy; // 예: "user_id DESC"
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder

    public static class Aggregates {
        private List<String> groupBy; // 예: ["user_id"]
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder

    public static class AggregatesFilter {
        private String field; // 예: "action"
        private String type;  // 예: "COUNT"
        private List<CaseWhen> caseWhen;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder

    public static class CaseWhen {
        private String when; // 예: "action = '페이지 방문'"
        private String then; // 예: "1"
    }
}
