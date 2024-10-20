package com.gogetdata.channel.application.dto;
import java.util.List;

public record ChannelListResponse(List<TeamChannelListResponse> responses) {
    public static ChannelListResponse from(List<TeamChannelListResponse> responses) {
        return new ChannelListResponse(
                responses
        );
    }
}
