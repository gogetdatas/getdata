package com.gogetdata.channel.application.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelDataResponse {
    private Map<String, Object> data;

}
