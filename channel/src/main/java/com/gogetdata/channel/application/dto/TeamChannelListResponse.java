package com.gogetdata.channel.application.dto;
import java.util.List;

public record TeamChannelListResponse(Long teamId,List<ChannelResponse> channelResponses) {
    public static TeamChannelListResponse from(Long teamId,final List<ChannelResponse>channelResponses) {
        return new TeamChannelListResponse(
                teamId,
                channelResponses
        );
    }

}
