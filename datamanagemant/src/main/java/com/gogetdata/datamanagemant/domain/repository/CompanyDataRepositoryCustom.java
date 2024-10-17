package com.gogetdata.datamanagemant.domain.repository;

import com.gogetdata.datamanagemant.application.dto.data.QueryResponse;
import com.gogetdata.datamanagemant.domain.entity.ChannelSetting;

import java.util.List;

public interface CompanyDataRepositoryCustom {
    List<QueryResponse> findChannelData(ChannelSetting channelSetting, Long companyId);

}
