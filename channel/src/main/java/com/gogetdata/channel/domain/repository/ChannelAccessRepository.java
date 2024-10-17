package com.gogetdata.channel.domain.repository;

import com.gogetdata.channel.domain.entity.ChannelAccess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelAccessRepository extends JpaRepository<ChannelAccess,Long> , ChannelAccessRepositoryCustom {
}
