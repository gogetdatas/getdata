package com.gogetdata.company.domain.repository.companyteamuser;

import com.gogetdata.company.domain.entity.CompanyTeam;
import com.gogetdata.company.domain.entity.CompanyTeamUser;

import java.util.List;

public interface CompanyTeamUserRepositoryCustom {
    boolean isExistAdminUser(Long companyTeamId , Long userId);
    boolean isExistAdminOrUser(Long companyTeamId , Long userId);
    CompanyTeamUser checkUserInTeam(Long companyTeamId , Long userId);
    List<CompanyTeamUser> isExistUsers(Long companyId,Long companyTeamId,List<Long>userIds); // 요청인놈들
    CompanyTeamUser isExistUser(Long companyId,Long companyTeamId,Long userId); // 요청인놈
    CompanyTeamUser isExistUser(Long companyId,Long companyTeamId,Long companyTeamUserId,Long userId); // 승인된놈
    List<CompanyTeam> getMyTeams(Long userId); // 내팀
    List<CompanyTeamUser> isExistUsers(Long companyTeamId); // 승인된놈

    List<CompanyTeamUser> getSearchTeamUser(Long companyTeamId, String userName);
}
