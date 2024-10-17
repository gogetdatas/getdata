package com.gogetdata.channel.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseCreateChannelRequest {
    String channelName;
    String type;
    Boolean isAggregates;
}
