package com.gogetdata.channel.application.dto;

import com.gogetdata.channel.domain.entity.Channel;

public record ChannelResponse (Long channelId , String ChannelName){
    public static ChannelResponse from(Channel channel) {
        return new ChannelResponse(
                channel.getChannelId(),
                channel.getChannelName()
        );
    }
}
