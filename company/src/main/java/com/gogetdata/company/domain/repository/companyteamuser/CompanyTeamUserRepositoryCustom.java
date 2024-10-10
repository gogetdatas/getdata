package com.gogetdata.company.domain.repository.companyteamuser;

import com.gogetdata.company.domain.entity.CompanyTeam;
import com.gogetdata.company.domain.entity.CompanyTeamUser;

import java.util.List;

public interface CompanyTeamUserRepositoryCustom {
    public boolean isExistAdminUser(Long companyTeamId , Long userId);
    public boolean isExistUserInTeam(Long companyTeamId , Long userId);
    public List<CompanyTeamUser> isExistUsers(Long companyTeamId,List<Long>userIds); // 요청인놈들
    public CompanyTeamUser isExistUser(Long companyTeamId,Long userId); // 요청인놈
    public List<CompanyTeam> getMyTeams(Long userId); // 내팀
    public List<CompanyTeamUser> isExistUsers(Long companyTeamId); // 승인된놈

    public CompanyTeamUser isApproveUser( Long companyTeamId, Long companyTeamUserId);
}
