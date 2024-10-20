package com.gogetdata.company.domain.repository.companyuser;

import com.gogetdata.company.domain.entity.AffiliationStatus;
import com.gogetdata.company.domain.entity.Company;
import com.gogetdata.company.domain.entity.CompanyUser;
import com.gogetdata.company.domain.entity.CompanyUserType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

import static com.gogetdata.company.domain.entity.QCompany.company;
import static com.gogetdata.company.domain.entity.QCompanyUser.companyUser;

@RequiredArgsConstructor

public class CompanyUserRepositoryImpl implements CompanyUserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Company getCompanyUserAdmin(Long userId, Long companyId) {
        return queryFactory.selectFrom(company)
                .join(companyUser).on(
                        company.companyId.eq(companyUser.companyId)
                                .and(company.isDeleted.eq(false))
                                .and(companyUser.status.eq(AffiliationStatus.APPROVED))
                                .and(companyUser.type.eq(CompanyUserType.ADMIN))
                )
                .where(
                        company.companyId.eq(companyId)
                                .and(companyUser.userId.eq(userId))
                )
                .fetchOne();
    }
    @Override
    public Company getCompanyUser(Long userId, Long companyId) {
        return queryFactory.selectFrom(company)
                .join(companyUser)
                .on(
                        company.companyId.eq(companyUser.companyId),
                        company.isDeleted.eq(false)
                        .and(companyUser.status.eq(AffiliationStatus.APPROVED))
                )
                .where(
                        company.companyId.eq(companyId)
                                .and(companyUser.userId.eq(userId))
                )
                .fetchOne();
    }
    @Override
    public List<CompanyUser> selectWaitingForApprovalUsers(Collection<Long> companyUserId, Long companyId) {
        return queryFactory
                .selectFrom(companyUser)
                .where(
                        companyUser.isDeleted.eq(false)
                                .and(companyUser.companyId.eq(companyId))
                                .and(companyUser.status.eq(AffiliationStatus.PENDING))
                                .and(companyUser.type.eq(CompanyUserType.UNASSIGN))
                                .and(companyUser.companyUserId.in(companyUserId))
                )
                .fetch();
    }

    @Override
    public List<CompanyUser> waitingForApprovalUsers(Long companyId) {
        return queryFactory
                .selectFrom(companyUser)
                .where(
                        companyUser.isDeleted.eq(false)
                                .and(companyUser.status.eq(AffiliationStatus.PENDING))
                                .and(companyUser.companyId.eq(companyId))
                )
                .fetch();
    }

    @Override
    public CompanyUser waitingForApprovalUser(Long companyId, Long companyUserId) {
        return queryFactory
                .selectFrom(companyUser)
                .where(
                        companyUser.isDeleted.eq(false)
                                .and(companyUser.companyId.eq(companyId))
                                .and(companyUser.companyUserId.eq(companyUserId))
                )
                .fetchOne();
    }

    @Override
    public List<CompanyUser> ApprovalUsers(Long companyId) {
        return queryFactory
                .selectFrom(companyUser)
                .where(
                        companyUser.isDeleted.eq(false)
                                .and(companyUser.status.eq(AffiliationStatus.APPROVED))
                                .and(companyUser.companyId.eq(companyId))
                )
                .fetch();
    }

    @Override
    public CompanyUser ApprovalUser(Long companyId, Long companyUserId) {
        return queryFactory
                .selectFrom(companyUser)
                .where(
                        companyUser.isDeleted.eq(false)
                                .and(companyUser.companyUserId.eq(companyUserId))
                                .and(companyUser.status.eq(AffiliationStatus.APPROVED))
                                .and(companyUser.companyId.eq(companyId))
                )
                .fetchOne();
    }

    @Override
    public CompanyUser isApprovalUser(Long companyId, Long userId) {
        return queryFactory
                .selectFrom(companyUser)
                .where(
                        companyUser.isDeleted.eq(false)
                                .and(companyUser.status.eq(AffiliationStatus.APPROVED))
                                .and(companyUser.userId.eq(userId))
                                .and(companyUser.companyId.eq(companyId))
                )
                .fetchOne();
    }

    @Override
    public List<Company> getSearchCompany(String companyName) {
        return queryFactory
                .selectFrom(company)
                .where(
                        company.isDeleted.eq(false)
                        .and(company.companyName.contains(companyName))
                )
                .fetch();
    }

    @Override
    public List<CompanyUser> getSearchApprovalUsers(Long companyId, String companyUserName) {
         return queryFactory.selectFrom(companyUser)
                .where(
                        companyUser.isDeleted.eq(false)
                                .and(companyUser.status.eq(AffiliationStatus.APPROVED))
                                .and(companyUser.companyId.eq(companyId))
                                .and(companyUser.userName.eq(companyUserName))
                )
                .fetch();

    }
}
