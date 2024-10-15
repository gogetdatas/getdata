package com.gogetdata.channel.application.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateChannelWithSubtypeRequest extends BaseCreateChannelRequest{
    private String subtype;
    private String selectKeyHash;
    private List<String> selectKeys;
}
