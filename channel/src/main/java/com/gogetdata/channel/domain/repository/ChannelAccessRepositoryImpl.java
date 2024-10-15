package com.gogetdata.channel.domain.repository;

import com.gogetdata.channel.domain.entity.Channel;
import com.gogetdata.channel.domain.entity.ChannelAccess;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.gogetdata.channel.domain.entity.QChannel.channel;
import static com.gogetdata.channel.domain.entity.QChannelAccess.channelAccess;
@RequiredArgsConstructor
public class ChannelAccessRepositoryImpl implements ChannelAccessRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public ChannelAccess findAccessChannel(Long companyTeamId, Long channelId) {
        return queryFactory
                .selectFrom(channelAccess)
                .where(
                        channelAccess.isDeleted.eq(false)
                                .and(channelAccess.companyTeamId.eq(companyTeamId))
                                .and(channelAccess.channelId.eq(channelId))
                )
                .fetchOne();
    }

    @Override
    public List<Channel> findsChannels(Long companyTeamId) {
        return queryFactory
                .selectFrom(channel)
                .innerJoin(channel)
                .on(channel.channelId.eq(channelAccess.channelId))
                .where(
                        channelAccess.isDeleted.eq(false)
                                .and(channel.isDeleted.eq(false))
                                .and(channelAccess.companyTeamId.eq(companyTeamId))
                )
                .fetch();
    }


}
