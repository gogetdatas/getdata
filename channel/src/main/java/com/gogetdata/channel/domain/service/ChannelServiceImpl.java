package com.gogetdata.channel.domain.service;

import com.gogetdata.channel.application.ChannelService;
import com.gogetdata.channel.application.CompanyTeamService;
import com.gogetdata.channel.application.DataManagementService;
import com.gogetdata.channel.application.dto.*;
import com.gogetdata.channel.domain.entity.Channel;
import com.gogetdata.channel.domain.entity.ChannelAccess;
import com.gogetdata.channel.domain.entity.ChannelAccessType;
import com.gogetdata.channel.domain.entity.ChannelSetting;
import com.gogetdata.channel.domain.repository.ChannelAccessRepository;
import com.gogetdata.channel.domain.repository.ChannelRepository;
import com.gogetdata.channel.domain.repository.ChannelSettingRepository;
import com.gogetdata.channel.infrastructrue.filter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelAccessRepository channelAccessRepository;
    private final CompanyTeamService companyTeamService;
    private final ChannelSettingRepository channelSettingRepository;
    private final DataManagementService dataManagementService;
    @Override
    @Transactional
    public MessageResponse createChannelTypeOnly(CreateChannelTypeOnlyRequest createChannelRequest,
                                                 CustomUserDetails customUserDetails, Long companyTeamId, Long companyId) {

        getAccessibleAdmin(customUserDetails , companyId,companyTeamId);
        Channel channel=  saveChannel(createChannelRequest.getChannelName(),companyTeamId);

        ChannelSetting channelSetting = ChannelSetting.builder()
                .companyId(companyId)
                .channelId(channel.getChannelId())
                .type(createChannelRequest.getType())
                .isAggregates(createChannelRequest.getIsAggregates())
                .build();
        channelSettingRepository.save(channelSetting);
        return MessageResponse.from("채널생성");
    }

    @Override
    @Transactional
    public MessageResponse createChannelWithSubtype(CreateChannelWithSubtypeRequest createChannelRequest,
                                                    CustomUserDetails customUserDetails, Long companyTeamId, Long companyId) {
        getAccessibleAdmin(customUserDetails , companyId,companyTeamId);
        Channel channel=  saveChannel(createChannelRequest.getChannelName(),companyTeamId);
        ChannelSetting channelSetting = ChannelSetting.builder()
                .companyId(companyId)
                .channelId(channel.getChannelId())
                .isAggregates(createChannelRequest.getIsAggregates())

                .type(createChannelRequest.getType())
                .subtype(createChannelRequest.getSubtype())
                .selectKeyHash(createChannelRequest.getSelectKeyHash())
                .selectKeys(createChannelRequest.getSelectKeys())
                .build();
        channelSettingRepository.save(channelSetting);
        return MessageResponse.from("채널생성");
    }

    @Override
    @Transactional
    public MessageResponse createChannelWithAggregation(CreateChannelWithAggregationRequest createChannelRequest,
                                                        CustomUserDetails customUserDetails, Long companyTeamId, Long companyId) {
        getAccessibleAdmin(customUserDetails, companyId, companyTeamId);
        Channel channel = saveChannel(createChannelRequest.getChannelName(), companyTeamId);

        List<ChannelSetting.AggregatesFilter> entityAggregatesFilters = createChannelRequest.getFilters().getAggregatesFilter()
                .stream()
                .map(dtoFilter -> ChannelSetting.AggregatesFilter.builder()
                        .field(dtoFilter.getField())
                        .type(dtoFilter.getType())
                        .caseWhen(
                                dtoFilter.getCaseWhen() != null ?
                                        dtoFilter.getCaseWhen()
                                                .stream()
                                                .map(dtoCaseWhen -> ChannelSetting.CaseWhen.builder()
                                                        .when(dtoCaseWhen.getWhen())
                                                        .then(dtoCaseWhen.getThen())
                                                        .build())
                                                .collect(Collectors.toList()) : null
                        )
                        .build())
                .collect(Collectors.toList());

        ChannelSetting channelSetting = ChannelSetting.builder()
                .companyId(companyId)
                .channelId(channel.getChannelId())
                .type(createChannelRequest.getType())
                .isAggregates(createChannelRequest.getIsAggregates())
                .subtype(createChannelRequest.getSubtype())
                .selectKeyHash(createChannelRequest.getSelectKeyHash())
                .selectKeys(createChannelRequest.getSelectKeys())
                .filters(ChannelSetting.Filters.builder()
                        .aggregates(ChannelSetting.Aggregates.builder()
                                .groupBy(createChannelRequest.getFilters().getAggregates().getGroupBy())
                                .build())
                        .aggregatesFilter(entityAggregatesFilters)
                        .orderBy(createChannelRequest.getFilters().getOrderBy())
                        .build())
                .build();
        channelSettingRepository.save(channelSetting);

        return MessageResponse.from("채널생성");
    }

    @Override
    @Transactional
    public MessageResponse deleteChannel(CustomUserDetails customUserDetails, Long companyTeamId, Long companyId, Long channelId) {
        getAccessibleAdmin(customUserDetails , companyId,companyTeamId);
        getAccessibleAdminChannel(companyTeamId,channelId);
        Channel channel=validateChannelNotDeleted(findChannel(channelId));
        channel.delete();
        channelRepository.save(channel);
        return MessageResponse.from("채널삭제");
    }



    @Override
    @Transactional
    public MessageResponse updateChannel(UpdateChannelNameRequest updateChannelNameRequest, CustomUserDetails customUserDetails, Long companyTeamId, Long companyId, Long channelId) {
        getAccessibleAdmin(customUserDetails , companyId,companyTeamId);
        getAccessibleAdminChannel(companyTeamId,channelId);
        Channel channel=validateChannelNotDeleted(findChannel(channelId));
        channel.updateName(updateChannelNameRequest.getChannelName());
        channelRepository.save(channel);
        return MessageResponse.from("채널수정");
    }

    @Override
    public List<ChannelDataResponse> getChannel(CustomUserDetails customUserDetails, Long companyTeamId, Long companyId, Long channelId) {
        getAccessible(customUserDetails , companyId,companyTeamId);
        return dataManagementService.findChannelData(channelId, companyId);
    }
    @Override
    public ChannelListResponse listChannels(CustomUserDetails customUserDetails, Long companyId, List<Long> companyTeamIds ) {
        List<TeamChannelListResponse> teamChannelListResponses = new ArrayList<>();
        for (Long companyTeamId : companyTeamIds) {
            List<ChannelResponse> channelResponses = new ArrayList<>();
            List<Channel> channels = channelAccessRepository.findsChannels(companyTeamId);
            for (Channel channel : channels) {
                channelResponses.add(ChannelResponse.from(channel));
            }
            teamChannelListResponses.add(TeamChannelListResponse.from(companyTeamId,channelResponses));
        }
        return ChannelListResponse.from(teamChannelListResponses);
    }

    @Override
    public MessageResponse updateChannelSetting(Long channelId, UpdateChannelWithAggregationRequest updateRequest,
                                     CustomUserDetails customUserDetails, Long companyTeamId, Long companyId) {
        getAccessibleAdmin(customUserDetails, companyId, companyTeamId);
        ChannelSetting existingChannelSetting = channelSettingRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널 설정을 찾을 수 없습니다."));

        existingChannelSetting.setType(updateRequest.getType());
        existingChannelSetting.setSubtype(updateRequest.getSubtype());
        existingChannelSetting.setSelectKeyHash(updateRequest.getSelectKeyHash());
        existingChannelSetting.setSelectKeys(updateRequest.getSelectKeys());

        if (updateRequest.getFilters() != null) {
            ChannelSetting.Filters existingFilters = existingChannelSetting.getFilters();
            UpdateChannelWithAggregationRequest.Filters updateFilters = updateRequest.getFilters();

            if (existingFilters == null) {
                existingFilters = new ChannelSetting.Filters();
                existingChannelSetting.setFilters(existingFilters);
            }

            if (updateFilters.getAggregates() != null) {
                existingFilters.setAggregates(ChannelSetting.Aggregates.builder()
                        .groupBy(updateFilters.getAggregates().getGroupBy())
                        .build());
            }

            if (updateFilters.getAggregatesFilter() != null) {
                List<ChannelSetting.AggregatesFilter> entityAggregatesFilters = updateFilters.getAggregatesFilter()
                        .stream()
                        .map(this::mapAggregatesFilter)
                        .collect(Collectors.toList());
                existingFilters.setAggregatesFilter(entityAggregatesFilters);
            }

            if (updateFilters.getOrderBy() != null) {
                existingFilters.setOrderBy(updateFilters.getOrderBy());
            }
        }

        channelSettingRepository.save(existingChannelSetting);

        return MessageResponse.from("채널 설정이 성공적으로 업데이트되었습니다.");
    }

    @Override
    public ChannelSettingResponse getChannelSetting(Long channelId, CustomUserDetails customUserDetails, Long companyTeamId, Long companyId) {
        getAccessible(customUserDetails, companyId, companyTeamId);

        ChannelSetting channelSetting = channelSettingRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널 설정을 찾을 수 없습니다."));
        return mapToChannelSettingResponse(channelSetting);
    }

    private void getAccessibleAdminChannel(Long companyTeamId,Long channelId) {
        if(!channelAccessRepository.findAccessChannel(companyTeamId,channelId).getChannelAccessType().getAuthority().equals("ADMIN")){
            throw new IllegalAccessError("권한없음");
        }
    }
    private void getAccessibleAdmin(CustomUserDetails customUserDetails, Long companyId, Long companyTeamId) {
        if (isAdmin(customUserDetails.getAuthorities().toString())) {
            return;
        }
        if(isCompany(companyId, customUserDetails.companyId())) {
            if(!isAdmin(customUserDetails.getCompanyType())){
                if (!companyTeamService.checkUserInTeam(companyTeamId, customUserDetails.userId()).equals("ADMIN")) {
                    throw new IllegalAccessError("권한없음");
                }
            }
        }else {
            throw new IllegalAccessError("권한없음");
        }
    }
    private void getAccessible(CustomUserDetails customUserDetails, Long companyId, Long companyTeamId) {
        if (isAdmin(customUserDetails.getAuthorities().toString())) {
            return;
        }
        if(isCompany(companyId, customUserDetails.companyId())) {
            if(!isAdmin(customUserDetails.getCompanyType())){
                if (!companyTeamService.checkUserInTeam(companyTeamId, customUserDetails.userId()).equals("null")) {
                    throw new IllegalAccessError("권한없음");
                }
            }
        }else {
            throw new IllegalAccessError("권한없음");
        }
    }

    private Channel saveChannel(String channelName, Long companyTeamId) {
        Channel channel = Channel.create(channelName);
        channelRepository.save(channel);
        ChannelAccess channelAccess = ChannelAccess.create(channel.getChannelId(), companyTeamId, ChannelAccessType.ADMIN);
        channelAccessRepository.save(channelAccess);
        return channel;
    }
    private boolean isAdmin(String role) {
        return role.equals("ADMIN");
    }
    private boolean isCompany(Long companyId,Long loginCompanyId) {
        if (Objects.equals(loginCompanyId, companyId)) {
            return true;
        } else {
            throw new IllegalAccessError("권한없음");
        }
    }
    private Channel findChannel(Long channelId){
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID로 채널을 찾을 수 없습니다: " + channelId));
    }
    private Channel validateChannelNotDeleted(Channel channel) {
        if (channel.isDeleted()) {
            throw new NoSuchElementException("삭제된 채널입니다: " + channel.getChannelName());
        }
        return channel;
    }
    private ChannelSetting.AggregatesFilter mapAggregatesFilter(UpdateChannelWithAggregationRequest.AggregatesFilter dtoFilter) {
        List<ChannelSetting.CaseWhen> entityCaseWhens = dtoFilter.getCaseWhen() != null ?
                dtoFilter.getCaseWhen()
                        .stream()
                        .map(dtoCaseWhen -> ChannelSetting.CaseWhen.builder()
                                .when(dtoCaseWhen.getWhen())
                                .then(dtoCaseWhen.getThen())
                                .build())
                        .collect(Collectors.toList()) : null;

        return ChannelSetting.AggregatesFilter.builder()
                .field(dtoFilter.getField())
                .type(dtoFilter.getType())
                .caseWhen(entityCaseWhens)
                .build();
    }

    private ChannelSettingResponse mapToChannelSettingResponse(ChannelSetting channelSetting) {
        if (channelSetting.getFilters() == null) {
            return ChannelSettingResponse.builder()
                    .companyId(channelSetting.getCompanyId())
                    .channelId(channelSetting.getChannelId())
                    .type(channelSetting.getType())
                    .subtype(channelSetting.getSubtype())
                    .selectKeyHash(channelSetting.getSelectKeyHash())
                    .selectKeys(channelSetting.getSelectKeys())
                    .filters(null)
                    .build();
        }

        ChannelSettingResponse.FiltersResponse filtersResponse = ChannelSettingResponse.FiltersResponse.builder()
                .aggregates(ChannelSettingResponse.AggregatesResponse.builder()
                        .groupBy(channelSetting.getFilters().getAggregates().getGroupBy())
                        .build())
                .aggregatesFilter(channelSetting.getFilters().getAggregatesFilter()
                        .stream()
                        .map(this::mapAggregatesFilterResponse)
                        .collect(Collectors.toList()))
                .orderBy(channelSetting.getFilters().getOrderBy())
                .build();

        return ChannelSettingResponse.builder()
                .companyId(channelSetting.getCompanyId())
                .channelId(channelSetting.getChannelId())
                .type(channelSetting.getType())
                .subtype(channelSetting.getSubtype())
                .selectKeyHash(channelSetting.getSelectKeyHash())
                .selectKeys(channelSetting.getSelectKeys())
                .filters(filtersResponse)
                .build();
    }
    private ChannelSettingResponse.AggregatesFilterResponse mapAggregatesFilterResponse(ChannelSetting.AggregatesFilter aggregatesFilter) {
        return ChannelSettingResponse.AggregatesFilterResponse.builder()
                .field(aggregatesFilter.getField())
                .type(aggregatesFilter.getType())
                .caseWhen(aggregatesFilter.getCaseWhen()
                        .stream()
                        .map(this::mapCaseWhenResponse)
                        .collect(Collectors.toList()))
                .build();
    }
    private ChannelSettingResponse.CaseWhenResponse mapCaseWhenResponse(ChannelSetting.CaseWhen caseWhen) {
        return ChannelSettingResponse.CaseWhenResponse.builder()
                .when(caseWhen.getWhen())
                .then(caseWhen.getThen())
                .build();
    }
}
