package com.gogetdata.channel.domain.repository;

import com.gogetdata.channel.domain.entity.ChannelSetting;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface  ChannelSettingRepository extends MongoRepository<ChannelSetting, Long> {
}
