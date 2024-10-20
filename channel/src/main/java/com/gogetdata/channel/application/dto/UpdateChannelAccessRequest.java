package com.gogetdata.channel.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateChannelAccessRequest {
    Long companyTeamId;
    String updateType;
}
