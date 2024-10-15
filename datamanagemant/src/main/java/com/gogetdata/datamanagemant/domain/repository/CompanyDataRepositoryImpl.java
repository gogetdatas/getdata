package com.gogetdata.datamanagemant.domain.repository;

import com.gogetdata.datamanagemant.application.dto.data.QueryRequest;
import com.gogetdata.datamanagemant.application.dto.data.QueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CompanyDataRepositoryImpl implements CompanyDataRepositoryCustom{
    private final MongoTemplate mongoTemplate;
    @Override
    public List<QueryResponse> findChannelData(QueryRequest queryRequest,Long companyId) {
        Criteria criteria = new Criteria();

        criteria = criteria.and("type").is(queryRequest.getType());
        criteria = criteria.and("companyId").is(companyId);

        if (queryRequest.getSubtype() != null) {
            criteria = criteria.and("subtype").is(queryRequest.getSubtype());
            criteria = criteria.and("keyHash").is(queryRequest.getHashKey());
        }
        Aggregation aggregation;
        if (queryRequest.isAggregates()) {
            aggregation = buildAggregationWithAggregates(criteria, queryRequest);
            AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "company_data", Map.class);
            List<Map> mappedResults = results.getMappedResults();
            return mappedResults.stream()
                    .map(map -> QueryResponse.builder().data(map).build())
                    .toList();
        } else {
            return buildAggregationWithoutAggregates(criteria,queryRequest);
        }
    }
    private Aggregation buildAggregationWithAggregates(Criteria criteria, QueryRequest queryRequest) {
        // Match Stage: 필터링
        MatchOperation matchStage = Aggregation.match(criteria);

        // Group Stage: 그룹화 기준 설정
        QueryRequest.Aggregates groupFilters = queryRequest.getFilters() != null
                ? queryRequest.getFilters().getAggregates()
                : null;

        GroupOperation groupStage;
        if (groupFilters != null && groupFilters.getGroup_by() != null && !groupFilters.getGroup_by().isEmpty()) {
            List<String> groupByFields = groupFilters.getGroup_by();
            groupStage = Aggregation.group(groupByFields.toArray(new String[0]));
        } else {
            // 그룹화 기준이 없으면 전체를 하나의 그룹으로 묶음
            groupStage = Aggregation.group().first("type").as("type");
        }

        // AggregatesFilter: 조건부 집계 추가
        List<QueryRequest.AggregatesFilter> aggregatesFilters = queryRequest.getFilters() != null
                ? queryRequest.getFilters().getAggregatesfilter()
                : null;

        if (aggregatesFilters != null && !aggregatesFilters.isEmpty()) {
            for (QueryRequest.AggregatesFilter filter : aggregatesFilters) {
                String type = filter.getType();   // 집계 타입 (예: COUNT)
                List<QueryRequest.CaseWhen> caseWhens = filter.getCase_when();

                for (QueryRequest.CaseWhen caseWhen : caseWhens) {
                    String whenCondition = caseWhen.getWhen(); // 예: "isget = 1"
                    String thenAlias = caseWhen.getThen();     // 예: "count_isget_1"

                    // 조건부 집계 표현식 생성
                    AggregationExpression conditionalExpression = ConditionalOperators
                            .when(buildMongoCondition(whenCondition))
                            .then(1)
                            .otherwise(0);

                    switch (type.toUpperCase()) {
                        case "COUNT":
                            groupStage = groupStage.sum(conditionalExpression).as(thenAlias);
                            break;
                        // 다른 집계 타입(CALCULATION, AVERAGE 등)도 추가 가능
                        default:
                            throw new UnsupportedOperationException("Unsupported aggregate type: " + type);
                    }
                }
            }
        }

        // Sort Stage: 정렬 (옵션)
        SortOperation sortStage = Aggregation.sort(Sort.by(Sort.Direction.ASC, "userid")); // 기본값

        if (queryRequest.getFilters() != null && queryRequest.getFilters().getOrderBy() != null
                && !queryRequest.getFilters().getOrderBy().isEmpty()) {
            String orderBy = queryRequest.getFilters().getOrderBy();
            String[] orderParts = orderBy.split(" ");
            String sortField = orderParts[0];
            Sort.Direction direction = Sort.Direction.ASC;
            if (orderParts.length > 1 && orderParts[1].equalsIgnoreCase("DESC")) {
                direction = Sort.Direction.DESC;
            }
            sortStage = Aggregation.sort(Sort.by(direction, sortField));
        }

        // Projection Stage: 필요한 필드만 선택 (옵션)
        ProjectionOperation projectStage = Aggregation.project();

        if (groupFilters != null && groupFilters.getGroup_by() != null && !groupFilters.getGroup_by().isEmpty()) {
            // 그룹화된 필드를 별도의 필드로 매핑
            for (String groupField : groupFilters.getGroup_by()) {
                projectStage = projectStage.and("_id." + groupField).as(groupField);
            }
        } else {
            // 기본 그룹화 시 타입 필드를 포함
            projectStage = projectStage.and("_id.type").as("type");
        }

        // 동적으로 집계 필드를 추가
        if (aggregatesFilters != null && !aggregatesFilters.isEmpty()) {
            for (QueryRequest.AggregatesFilter filter : aggregatesFilters) {
                List<QueryRequest.CaseWhen> caseWhens = filter.getCase_when();
                for (QueryRequest.CaseWhen caseWhen : caseWhens) {
                    String thenAlias = caseWhen.getThen();
                    projectStage = projectStage.and(thenAlias).as(thenAlias);
                }
            }
        }

        // Aggregation Pipeline 구성

        return Aggregation.newAggregation(
                matchStage,
                groupStage,
                sortStage,
                projectStage
        );
    }

    /**
     * 단순 조회를 위한 Query 객체를 기반으로 문서를 조회하고 결과를 반환합니다.
     */
    private List<QueryResponse> buildAggregationWithoutAggregates(Criteria criteria, QueryRequest queryRequest) {
        // 1. Query 객체 생성 및 필터링 조건 설정
        Query query = new Query(criteria);

        // 2. 정렬 조건 추가 (옵션)
        if (queryRequest.getFilters() != null && queryRequest.getFilters().getOrderBy() != null
                && !queryRequest.getFilters().getOrderBy().isEmpty()) {
            String orderBy = queryRequest.getFilters().getOrderBy();
            String[] orderParts = orderBy.split(" ");
            String sortField = orderParts[0];
            Sort.Direction direction = Sort.Direction.ASC;
            if (orderParts.length > 1 && orderParts[1].equalsIgnoreCase("DESC")) {
                direction = Sort.Direction.DESC;
            }
            query.with(Sort.by(direction, sortField));
        }
        // 4. 단순 조회 실행
        List<Map> mappedResults = mongoTemplate.find(query, Map.class, "company_data");

        // 5. Map을 QueryResponse로 변환하여 반환
        return mappedResults.stream()
                .map(map -> QueryResponse.builder().data(map).build())
                .toList();
    }

    /**
     * when 조건을 기반으로 MongoDB의 $cond 표현식을 생성합니다.
     *
     * @param whenCondition 예: "isget = 1"
     * @return 조건 표현식
     */
    private Criteria buildMongoCondition(String whenCondition) {
        // 간단한 파싱 로직: "field = value"
        String[] parts = whenCondition.split("=");
        if (parts.length == 2) {
            String field = parts[0].trim();
            String valueStr = parts[1].trim();
            Object value;

            try {
                value = Long.parseLong(valueStr);
            } catch (NumberFormatException e) {
                try {
                    value = Double.parseDouble(valueStr);
                } catch (NumberFormatException ex) {
                    value = valueStr.replaceAll("\"", "");
                }
            }

            return Criteria.where(field).is(value);
        }
        throw new IllegalArgumentException("Invalid when condition: " + whenCondition);
    }
}
