package com.gogetdata.company.domain.repository.companyteamuser;

import com.gogetdata.company.domain.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.gogetdata.company.domain.entity.QCompanyTeamUser.companyTeamUser;
import static com.gogetdata.company.domain.entity.QCompanyUser.companyUser;
import static com.gogetdata.company.domain.entity.QCompanyTeam.companyTeam;
@RequiredArgsConstructor
public class CompanyTeamUserRepositoryImpl implements CompanyTeamUserRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isExistAdminUser(Long companyTeamId, Long userId) {
        return queryFactory.selectFrom(companyTeam)
                .where(companyTeamUser.userId.eq(userId)
                        .and(companyTeamUser.companyTeamId.eq(companyTeamId))
                        .and(companyTeamUser.isDeleted.eq(false))
                        .and(companyTeamUser.companyTeamUserStatus.eq(CompanyTeamUserStatus.APPROVED))
                        .and(companyTeamUser.companyTeamUserType.eq(CompanyTeamUserType.ADMIN))
                )
                .fetchOne() != null;
    }

    @Override
    public boolean isExistUserInTeam(Long companyTeamId, Long userId) {
        return queryFactory.selectFrom(companyTeam)
                .where(companyTeamUser.userId.eq(userId)
                        .and(companyTeamUser.companyTeamId.eq(companyTeamId))
                        .and(companyTeamUser.isDeleted.eq(false))
                        .and(companyTeamUser.companyTeamUserStatus.eq(CompanyTeamUserStatus.APPROVED))
                        .or(companyTeamUser.companyTeamUserType.eq(CompanyTeamUserType.ADMIN))
                        .or(companyTeamUser.companyTeamUserType.eq(CompanyTeamUserType.USER))
                )
                .fetchOne() != null;
    }

    @Override
    public List<CompanyTeamUser> isExistUsers(Long companyTeamId, List<Long> userIds) {
        return queryFactory.select(companyTeamUser)
                .from(companyTeamUser)
                .where(companyTeamUser.companyTeamId.eq(companyTeamId)
                        .and(companyTeamUser.userId.in(userIds))
                        .and(companyTeamUser.companyTeamUserStatus.eq(CompanyTeamUserStatus.PENDING))
                        .and(companyTeamUser.isDeleted.eq(false))
                )
                .fetch();
    }

    @Override
    public CompanyTeamUser isExistUser(Long companyTeamId, Long userId) {
        return queryFactory.select(companyTeamUser)
                .from(companyTeamUser)
                .where(companyTeamUser.companyTeamId.eq(companyTeamId)
                        .and(companyTeamUser.companyTeamUserId.eq(userId))
                        .and(companyTeamUser.companyTeamUserStatus.eq(CompanyTeamUserStatus.PENDING))
                        .and(companyTeamUser.isDeleted.eq(false))
                )
                .fetchOne();
    }
    @Override
    public List<CompanyTeam> getMyTeams(Long userId) {
        return queryFactory.select(companyTeam)
                .from(companyTeam)
                .innerJoin(companyTeam).on(companyTeam.companyTeamId.eq(companyTeamUser.companyTeamId))
                .where(companyTeamUser.userId.eq(userId)
                        .and(companyTeamUser.companyTeamUserId.eq(userId))
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
    public CompanyTeamUser isApproveUser(Long companyTeamId, Long companyTeamUserId) {
        return queryFactory.select(companyTeamUser)
                .from(companyTeamUser)
                .where(companyTeamUser.companyTeamId.eq(companyTeamId)
                        .and(companyTeamUser.companyTeamUserId.eq(companyTeamUserId))
                        .and(companyTeamUser.companyTeamUserStatus.eq(CompanyTeamUserStatus.APPROVED))
                        .and(companyTeamUser.isDeleted.eq(false))
                )
                .fetchOne();
    }
}
