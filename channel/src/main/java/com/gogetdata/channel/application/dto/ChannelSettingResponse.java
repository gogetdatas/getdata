package com.gogetdata.channel.application.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelSettingResponse {

    private Long companyId;
    private Long channelId;
    private String type;
    private String subtype;
    private String selectKeyHash;
    private List<String> selectKeys;
    private FiltersResponse filters;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FiltersResponse {
        private AggregatesResponse aggregates;
        private List<AggregatesFilterResponse> aggregatesFilter;
        private String orderBy;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AggregatesResponse {
        private List<String> groupBy;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AggregatesFilterResponse {
        private String field;
        private String type;
        private List<CaseWhenResponse> caseWhen;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CaseWhenResponse {
        private String when;
        private String then;
    }
}
