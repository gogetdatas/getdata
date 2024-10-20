package com.gogetdata.channel.application;

import com.gogetdata.channel.domain.entity.ChannelAccess;
import com.gogetdata.channel.domain.entity.ChannelAccessType;

public record ChannelAccessResponse(Long channelAccessId , Long channelId , Long companyTeamId , ChannelAccessType channelAccessType ) {
    public static ChannelAccessResponse from(ChannelAccess channelAccess) {
        return new ChannelAccessResponse(
                channelAccess.getChannelAccessId(),
                channelAccess.getChannelId(),
                channelAccess.getCompanyTeamId(),
                channelAccess.getChannelAccessType()
        );
    }
}
