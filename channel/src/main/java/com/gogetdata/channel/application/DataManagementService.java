package com.gogetdata.channel.application;

import com.gogetdata.channel.application.dto.ChannelDataResponse;
import com.gogetdata.channel.application.dto.QueryRequest;

import java.util.List;

public interface DataManagementService {
    List<ChannelDataResponse> findChannelData(QueryRequest request, Long companyId);
}
