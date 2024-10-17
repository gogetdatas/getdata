package com.gogetdata.datamanagemant.domain.repository;

import com.gogetdata.datamanagemant.application.dto.data.QueryResponse;
import com.gogetdata.datamanagemant.domain.entity.ChannelSetting;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class CompanyDataRepositoryImpl implements CompanyDataRepositoryCustom {

    private final MongoTemplate mongoTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CompanyDataRepositoryImpl.class);

    @Override
    @Transactional
    public List<QueryResponse> findChannelData(ChannelSetting channelSetting, Long companyId) {
        // 1. 필터링 기준(criteria) 설정
        Criteria criteria = buildCriteria(channelSetting, companyId);

        // 2. 집계가 활성화되어 있고, 필터가 존재하는 경우 집계 파이프라인 구성 및 실행
        if (channelSetting.isAggregates()
                && channelSetting.getFilters() != null
                && channelSetting.getFilters().getAggregates() != null) {
            Aggregation aggregation = buildAggregationWithAggregates(channelSetting, criteria);
            logger.debug("Executing Aggregation Pipeline: {}", aggregation.toString());
            AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "company_data", Map.class);
            return results.getMappedResults().stream()
                    .map(map -> QueryResponse.builder().data(map).build())
                    .collect(Collectors.toList());
        } else {
            // 3. 집계가 비활성화된 경우 단순 조회 실행
            return buildAggregationWithoutAggregates(channelSetting, criteria);
        }
    }


    private Criteria buildCriteria(ChannelSetting channelSetting, Long companyId) {
        Criteria criteria = new Criteria();
        criteria = criteria.and("type").is(channelSetting.getType());
        criteria = criteria.and("companyId").is(companyId);

        if (channelSetting.getSubtype() != null) {
            criteria = criteria.and("subtype").is(channelSetting.getSubtype());
            criteria = criteria.and("keyHash").is(channelSetting.getSelectKeyHash());
        }

        return criteria;
    }


    private Aggregation buildAggregationWithAggregates(ChannelSetting channelSetting, Criteria criteria) {
        List<AggregationOperation> operations = new ArrayList<>();

        // 1. Match Stage: 필터링 조건 적용
        operations.add(Aggregation.match(criteria));

        // 2. Group Stage: groupBy 필드와 집계 필터에 따라 그룹화 및 집계
        ChannelSetting.Aggregates aggregatesConfig = channelSetting.getFilters().getAggregates();
        List<String> groupByFields = aggregatesConfig.getGroupBy().stream()
                .map(this::resolveFieldPath) // 자동 변환 적용
                .collect(Collectors.toList());

        // 그룹화 필드가 없으면 예외 발생
        if (groupByFields.isEmpty()) {
            throw new IllegalArgumentException("groupBy fields cannot be null or empty");
        }

        // 그룹화 필드들을 기준으로 그룹화
        GroupOperation groupOperation = Aggregation.group(groupByFields.toArray(new String[0]));

        // 집계 필터에 따라 다양한 집계 연산 추가
        List<ChannelSetting.AggregatesFilter> aggregatesFilters = channelSetting.getFilters().getAggregatesFilter();
        if (aggregatesFilters != null && !aggregatesFilters.isEmpty()) {
            for (ChannelSetting.AggregatesFilter filter : aggregatesFilters) {
                String field = resolveFieldPath(filter.getField()); // 자동 변환 적용
                String type = filter.getType().toUpperCase();

                switch (type) {
                    case "COUNT":
                        if (filter.getCaseWhen() != null && !filter.getCaseWhen().isEmpty()) {
                            // 조건부 카운트 (CASE WHEN)
                            for (ChannelSetting.CaseWhen caseWhen : filter.getCaseWhen()) {
                                Criteria caseCriteria = parseCaseWhen(caseWhen.getWhen());
                                AggregationExpression conditionalExpression = ConditionalOperators
                                        .when(caseCriteria)
                                        .then(1)
                                        .otherwise(0);
                                groupOperation = groupOperation.sum(conditionalExpression).as(caseWhen.getThen());
                            }
                        } else {
                            // 단순 카운트
                            groupOperation = groupOperation.count().as("count");
                        }
                        break;
                    case "SUM":
                        groupOperation = groupOperation.sum(field).as(field + "_sum");
                        break;
                    case "AVERAGE":
                        groupOperation = groupOperation.avg(field).as(field + "_avg");
                        break;
                    // 필요한 다른 집계 연산 추가 가능
                    default:
                        throw new IllegalArgumentException("Unsupported aggregate type: " + type);
                }
            }
        }

        operations.add(groupOperation);

        // 3. Projection Stage: 결과 필드 매핑
        AggregationOperation projectionOperation = buildProjectionOperation(groupByFields, aggregatesFilters);
        operations.add(projectionOperation);

        // 4. Sort Stage: orderBy 조건에 따라 정렬
        String orderBy = channelSetting.getFilters().getOrderBy();
        AggregationOperation sortOperation = buildSortOperation(orderBy);
        if (sortOperation != null) {
            operations.add(sortOperation);
        }

        // Aggregation Pipeline 구성
        Aggregation aggregation = Aggregation.newAggregation(operations);

        return aggregation;
    }


    private Criteria parseCaseWhen(String whenCondition) {
        // 예: "action = '페이지 방문'"을 "action" 필드가 "페이지 방문"인 조건으로 변환
        String[] parts = whenCondition.split("=");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid caseWhen condition: " + whenCondition);
        }
        String field = parts[0].trim();
        String value = parts[1].trim().replace("'", "");
        return Criteria.where(field).is(value);
    }


    private AggregationOperation buildSortOperation(String orderBy) {
        if (orderBy == null || orderBy.isEmpty()) {
            return null;
        }
        String[] orderParts = orderBy.split(" ");
        String sortField = orderParts[0];
        Sort.Direction direction = Sort.Direction.ASC;
        if (orderParts.length > 1 && orderParts[1].equalsIgnoreCase("DESC")) {
            direction = Sort.Direction.DESC;
        }
        return Aggregation.sort(Sort.by(direction, sortField));
    }


    private ProjectionOperation buildProjectionOperation(List<String> groupByFields,
                                                         List<ChannelSetting.AggregatesFilter> aggregatesFilters) {
        ProjectionOperation projection = Aggregation.project();

        if (groupByFields.size() == 1) {
            String groupByField = groupByFields.get(0);
            String alias = decapitalize(getLastFieldName(groupByField));
            // 그룹화 필드가 하나일 경우, _id는 해당 필드의 값
            projection = projection.and("_id").as(alias);
        } else {
            for (String groupByField : groupByFields) {
                String alias = decapitalize(getLastFieldName(groupByField));
                projection = projection.and("_id." + groupByField).as(alias);
            }
        }

        // 집계 결과 매핑
        if (aggregatesFilters != null && !aggregatesFilters.isEmpty()) {
            for (ChannelSetting.AggregatesFilter filter : aggregatesFilters) {
                String type = filter.getType().toUpperCase();
                String field = resolveFieldPath(filter.getField()); // 자동 변환 적용

                switch (type) {
                    case "COUNT":
                        if (filter.getCaseWhen() != null && !filter.getCaseWhen().isEmpty()) {
                            for (ChannelSetting.CaseWhen caseWhen : filter.getCaseWhen()) {
                                projection = projection.and(caseWhen.getThen()).as(caseWhen.getThen());
                            }
                        } else {
                            projection = projection.and("count").as("count");
                        }
                        break;
                    case "SUM":
                        projection = projection.and(field + "_sum").as(field + "_sum");
                        break;
                    case "AVERAGE":
                        projection = projection.and(field + "_avg").as(field + "_avg");
                        break;
                    // 필요한 다른 집계 연산 추가 가능
                    default:
                        throw new IllegalArgumentException("Unsupported aggregate type in projection: " + type);
                }
            }
        }

        return projection;
    }


    private List<QueryResponse> buildAggregationWithoutAggregates(ChannelSetting channelSetting, Criteria criteria) {
        Query query = new Query(criteria);
        List<Map> mappedResults = mongoTemplate.find(query, Map.class, "company_data");

        return mappedResults.stream()
                .map(map -> QueryResponse.builder().data(map).build())
                .collect(Collectors.toList());
    }


    private String getLastFieldName(String fieldPath) {
        String[] parts = fieldPath.split("\\.");
        return parts[parts.length - 1];
    }


    private String decapitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0,1).toLowerCase() + str.substring(1);
    }

    private String resolveFieldPath(String field) {
        if (field.startsWith("data.")) {
            return field;
        }
        return "data." + field;
    }
}
