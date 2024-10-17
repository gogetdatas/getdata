package com.gogetdata.datamanagemant.domain.repository;

import com.gogetdata.datamanagemant.domain.entity.ChannelSetting;
import com.gogetdata.datamanagemant.domain.entity.CompanyData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChannelSettingRepository extends MongoRepository<ChannelSetting, Long>  {
    ChannelSetting findByChannelId(Long channelId);
}
