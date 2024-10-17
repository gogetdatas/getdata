package com.gogetdata.channel.domain.repository;

import com.gogetdata.channel.domain.entity.Channel;
import com.gogetdata.channel.domain.entity.ChannelAccess;

import java.util.List;

public interface ChannelAccessRepositoryCustom {
    ChannelAccess findAccessChannel(Long companyTeamId, Long channelId);
    List<Channel> findsChannels(Long companyTeamId);
    List<ChannelAccess> findsAccessChannel(Long channelId);
}
