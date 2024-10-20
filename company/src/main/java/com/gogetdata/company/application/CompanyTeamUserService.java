package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyteam.CompanyTeamResponse;
import com.gogetdata.company.application.dto.companyteamuser.AcceptJoinRequest;
import com.gogetdata.company.application.dto.companyteamuser.CompanyTeamUserResponse;
import com.gogetdata.company.application.dto.companyteamuser.UpdateUserPerMissionRequest;
import com.gogetdata.company.infrastructure.filter.CustomUserDetails;

import java.util.List;

public interface CompanyTeamUserService {
    MessageResponse applyToJoinTeam (CustomUserDetails customUserDetails, Long companyId, Long companyTeam);// 요청
    List<MessageResponse> acceptJoinRequest(CustomUserDetails customUserDetails , Long companyId , Long companyTeamId , List<AcceptJoinRequest> acceptJoinRequest);// 수락
    MessageResponse rejectJoinRequest(CustomUserDetails customUserDetails , Long companyId , Long companyTeamId,Long userId);// 거절
    MessageResponse updateUserPermission(CustomUserDetails customUserDetails , Long companyId , Long companyTeamId, Long userId,
                                                UpdateUserPerMissionRequest updateUserPerMissionRequest);// 권한변경
    List<CompanyTeamResponse> getMyTeams(CustomUserDetails customUserDetails);// 내 팀 조회
    List<CompanyTeamUserResponse> getUsersInTeam(CustomUserDetails customUserDetails , Long companyTeamId);// 팀에 속한 유저조회
    MessageResponse deleteUserFromTeam(CustomUserDetails customUserDetails , Long companyId , Long companyTeamId,Long companyTeamUserId);// 유저 삭제
    List<CompanyTeamUserResponse> searchTeamUser(CustomUserDetails customUserDetails , Long companyId,Long companyTeamId,String userName); // 팀내 유저 검색

    String getUserInTeam(Long companyTeamId, Long userId);

}
