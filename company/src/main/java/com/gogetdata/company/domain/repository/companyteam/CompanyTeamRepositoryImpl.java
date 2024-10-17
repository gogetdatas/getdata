package com.gogetdata.company.domain.repository.companyteam;

import com.gogetdata.company.domain.entity.CompanyTeam;
import com.gogetdata.company.domain.entity.CompanyTeamStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.gogetdata.company.domain.entity.QCompanyTeam.companyTeam;

@RequiredArgsConstructor
public class CompanyTeamRepositoryImpl implements CompanyTeamRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CompanyTeam> readRequestCompanyTeams(Long companyId) {
        return queryFactory
                .selectFrom(companyTeam)
                .where(
                        companyTeam.companyId.eq(companyId)
                        .and(companyTeam.isDeleted.eq(false))
                        .and(companyTeam.companyTeamStatus.eq(CompanyTeamStatus.PENDING))
                )
                .fetch();
    }

    @Override
    public CompanyTeam readRequestCompanyTeam(Long companyTeamId) {
        return queryFactory
                .selectFrom(companyTeam)
                .where(
                        companyTeam.companyTeamId.eq(companyTeamId)
                                .and(companyTeam.isDeleted.eq(false))
                                .and(companyTeam.companyTeamStatus.eq(CompanyTeamStatus.PENDING))
                )
                .fetchOne();
    }

    @Override
    public List<CompanyTeam> getSearchCompanyTeam(Long companyId, String companyTeamName) {
        return queryFactory
                .selectFrom(companyTeam)
                .where(
                        companyTeam.companyId.eq(companyId)
                                .and(companyTeam.isDeleted.eq(false))
                                .and(companyTeam.companyTeamStatus.eq(CompanyTeamStatus.APPROVED))
                                .and(companyTeam.companyTeamName.contains(companyTeamName))
                )
                .fetch();
    }
}
