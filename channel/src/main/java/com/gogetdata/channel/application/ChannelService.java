package com.gogetdata.channel.application;

import com.gogetdata.channel.application.dto.*;
import com.gogetdata.channel.infrastructrue.filter.CustomUserDetails;

import java.util.List;

public interface ChannelService {
    MessageResponse createChannelTypeOnly(CreateChannelTypeOnlyRequest createChannelRequest,
                                                 CustomUserDetails customUserDetails,
                                                 Long companyTeamId,
                                                 Long companyId);// 채널 생성
    MessageResponse createChannelWithSubtype(CreateChannelWithSubtypeRequest createChannelRequest,
                                                    CustomUserDetails customUserDetails,
                                                    Long companyTeamId,
                                                    Long companyId);// 채널 생성
    MessageResponse createChannelWithAggregation(CreateChannelWithAggregationRequest createChannelRequest,
                                                        CustomUserDetails customUserDetails,
                                                        Long companyTeamId,
                                                        Long companyId);// 채널 생성
    MessageResponse deleteChannel(CustomUserDetails customUserDetails,
                                  Long companyTeamId,
                                  Long companyId,
                                  Long channelId);// 채널 삭제
    MessageResponse updateChannel(UpdateChannelNameRequest updateChannelNameRequest,
                                  CustomUserDetails customUserDetails,
                                  Long companyTeamId,
                                  Long companyId,
                                  Long channelId);// 채널 수정
    List<ChannelDataResponse> getChannel(CustomUserDetails customUserDetails,
                    Long companyTeamId,
                    Long companyId,
                    Long channelId); // 채널 조회
    ChannelListResponse listChannels(CustomUserDetails customUserDetails,
                                     Long companyId, List<Long> companyTeamIds); // 가지고 있는 채널 목록 조회

    MessageResponse updateChannelSetting(Long channelId, UpdateChannelWithAggregationRequest updateRequest,
                              CustomUserDetails customUserDetails, Long companyTeamId, Long companyId);// 채널 설정 수정
    ChannelSettingResponse getChannelSetting(Long channelId, CustomUserDetails customUserDetails, Long companyTeamId, Long companyId); // 채널 설정 조회
}
