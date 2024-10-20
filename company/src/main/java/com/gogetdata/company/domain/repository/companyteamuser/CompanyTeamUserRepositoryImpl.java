package com.gogetdata.company.domain.repository.companyteamuser;

import com.gogetdata.company.domain.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.gogetdata.company.domain.entity.QCompanyTeamUser.companyTeamUser;
import static com.gogetdata.company.domain.entity.QCompanyUser.companyUser;
import static com.gogetdata.company.domain.entity.QCompanyTeam.companyTeam;

@RequiredArgsConstructor
public class CompanyTeamUserRepositoryImpl implements CompanyTeamUserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isExistAdminUser(Long companyTeamId, Long userId) {
        return queryFactory.selectOne()
                .from(companyUser)
                .leftJoin(companyTeamUser)
                .on(companyTeamUser.userId.eq(companyUser.userId)
                        .and(companyTeamUser.companyTeamId.eq(companyTeamId))
                        .and(companyTeamUser.isDeleted.eq(false))
                        .and(companyTeamUser.companyTeamUserStatus.eq(CompanyTeamUserStatus.APPROVED))
                        .and(companyTeamUser.companyTeamUserType.eq(CompanyTeamUserType.ADMIN))
                )
                .where(companyUser.userId.eq(userId)
                        .and(
                                companyUser.type.eq(CompanyUserType.ADMIN)
                                        .or(companyTeamUser.companyTeamUserType.eq(CompanyTeamUserType.ADMIN))
                        )
                )
                .fetchFirst() != null;
    }

    @Override
    public boolean isExistAdminOrUser(Long companyTeamId, Long userId) {
        return queryFactory.selectOne()
                .from(companyUser)
                .leftJoin(companyTeamUser)
                .on(companyTeamUser.userId.eq(companyUser.userId)
                        .and(companyTeamUser.companyTeamId.eq(companyTeamId))
                        .and(companyTeamUser.isDeleted.eq(false))
                        .and(companyTeamUser.companyTeamUserStatus.eq(CompanyTeamUserStatus.APPROVED))
                        .and(companyTeamUser.companyTeamUserType.in(
                                CompanyTeamUserType.ADMIN,
                                CompanyTeamUserType.USER))
                )
                .where(companyUser.userId.eq(userId)
                        .and(
                                companyUser.type.eq(CompanyUserType.ADMIN)
                                        .or(companyTeamUser.companyTeamUserType.in(
                                                CompanyTeamUserType.ADMIN,
                                                CompanyTeamUserType.USER))
                        )
                )
                .fetchFirst() != null;
    }

    @Override
    public CompanyTeamUser checkUserInTeam(Long companyTeamId, Long userId) {
        return queryFactory.select(companyTeamUser)
                .from(companyTeamUser)
                .where(companyTeamUser.userId.eq(userId)
                        .and(companyTeamUser.isDeleted.eq(false))
                        .or(companyTeamUser.companyTeamUserType.in(
                                CompanyTeamUserType.ADMIN,
                                CompanyTeamUserType.USER))
                )
                .fetchFirst();
    }

    @Override
    public List<CompanyTeamUser> isExistUsers(Long companyId, Long companyTeamId, List<Long> userIds) {
        return queryFactory.select(companyTeamUser)
                .from(companyTeamUser)
                .innerJoin(companyTeamUser).on(companyTeamUser.userId.eq(companyUser.userId))
                .where(companyTeamUser.companyTeamId.eq(companyTeamId)
                        .and(companyUser.companyId.eq(companyId))
                        .and(companyTeamUser.userId.in(userIds))
                        .and(companyTeamUser.companyTeamUserStatus.eq(CompanyTeamUserStatus.PENDING))
                        .and(companyTeamUser.isDeleted.eq(false))
                        .and(companyUser.isDeleted.eq(false))
                )
                .fetch();
    }

    @Override
    public CompanyTeamUser isExistUser(Long companyId, Long companyTeamId, Long userId) {
        return queryFactory.select(companyTeamUser)
                .from(companyTeamUser)
                .innerJoin(companyTeamUser).on(companyTeamUser.userId.eq(companyUser.userId))
                .where(companyTeamUser.companyTeamId.eq(companyTeamId)
                        .and(companyUser.companyId.eq(companyId))
                        .and(companyTeamUser.companyTeamUserId.eq(userId))
                        .and(companyTeamUser.companyTeamUserStatus.eq(CompanyTeamUserStatus.PENDING))
                        .and(companyTeamUser.isDeleted.eq(false))
                        .and(companyUser.isDeleted.eq(false))
                )
                .fetchOne();
    }

    @Override
    public CompanyTeamUser isExistUser(Long companyId, Long companyTeamId, Long companyTeamUserId, Long userId) {
        return queryFactory.select(companyTeamUser)
                .from(companyTeamUser)
                .innerJoin(companyTeamUser).on(companyTeamUser.companyTeamUserId.eq(companyUser.userId))
                .where(companyTeamUser.companyTeamId.eq(companyTeamId)
                        .and(companyUser.companyId.eq(companyId))
                        .and(companyTeamUser.companyTeamUserId.eq(userId))
                        .and(companyTeamUser.companyTeamUserStatus.eq(CompanyTeamUserStatus.APPROVED))
                        .and(companyTeamUser.isDeleted.eq(false))
                        .and(companyUser.isDeleted.eq(false))
                )
                .fetchOne();
    }

    @Override
    public List<CompanyTeam> getMyTeams(Long userId) {
        return queryFactory.select(companyTeam)
                .from(companyTeam)
                .innerJoin(companyTeamUser)
                .on(companyTeam.companyTeamId.eq(companyTeamUser.companyTeamId))
                .where(companyTeamUser.userId.eq(userId)
                        .and(companyTeam.companyTeamStatus.eq(CompanyTeamStatus.APPROVED))
                        .and(companyTeamUser.isDeleted.eq(false))
                        .and(companyTeam.isDeleted.eq(false))
                )
                .fetch();
    }

    @Override
    public List<CompanyTeamUser> isExistUsers(Long companyTeamId) {
        return queryFactory.select(companyTeamUser)
                .from(companyTeamUser)
                .where(companyTeamUser.companyTeamId.eq(companyTeamId)
                        .and(companyTeamUser.companyTeamUserStatus.eq(CompanyTeamUserStatus.APPROVED))
                        .and(companyTeamUser.isDeleted.eq(false))
                )
                .fetch();
    }

    @Override
    public List<CompanyTeamUser> getSearchTeamUser(Long companyTeamId, String userName) {
        return queryFactory.select(companyTeamUser)
                .from(companyTeamUser)
                .where(companyTeamUser.companyTeamId.eq(companyTeamId)
                        .and(companyTeamUser.companyTeamUserStatus.eq(CompanyTeamUserStatus.APPROVED))
                        .and(companyTeamUser.isDeleted.eq(false))
                        .and(companyTeamUser.userName.contains(userName))
                )
                .fetch();
    }
}
