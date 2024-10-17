package com.gogetdata.channel.application;

import com.gogetdata.channel.application.dto.ChannelDataResponse;

import java.util.List;

public interface DataManagementService {
    List<ChannelDataResponse> findChannelData(Long companyId, Long channelId);
}
