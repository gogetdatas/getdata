package com.gogetdata.channel.domain.repository;

import com.gogetdata.channel.domain.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ChannelRepository extends JpaRepository<Channel,Long> {
}
