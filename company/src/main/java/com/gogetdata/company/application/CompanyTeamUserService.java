package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyteam.CompanyTeamResponse;
import com.gogetdata.company.application.dto.companyteamuser.AcceptJoinRequest;
import com.gogetdata.company.application.dto.companyteamuser.CompanyTeamUserResponse;
import com.gogetdata.company.application.dto.companyteamuser.UpdateUserPerMissionRequest;
import com.gogetdata.company.infrastructure.filter.CustomUserDetails;

import java.util.List;

public interface CompanyTeamUserService {
    public MessageResponse applyToJoinTeam (CustomUserDetails userDetails, Long companyId,Long companyTeam);// 요청
    public List<MessageResponse> acceptJoinRequest(CustomUserDetails userDetails , Long companyId , Long companyTeamId , List<AcceptJoinRequest> acceptJoinRequest);// 수락
    public MessageResponse rejectJoinRequest(CustomUserDetails userDetails , Long companyId , Long companyTeamId,Long userId);// 거절
    public MessageResponse updateUserPermission(CustomUserDetails userDetails , Long companyId , Long companyTeamId, Long userId,
                                                UpdateUserPerMissionRequest updateUserPerMissionRequest);// 권한변경
    public List<CompanyTeamResponse> getMyTeams(CustomUserDetails userDetails);// 내 팀 조회
    public List<CompanyTeamUserResponse> getUsersInTeam(CustomUserDetails userDetails , Long companyTeamId);// 팀에 속한 유저조회
    public MessageResponse deleteUserFromTeam(CustomUserDetails userDetails , Long companyId , Long companyTeamId,Long companyTeamUserId);// 유저 삭제

    // 유저 정보 업데이트 // 유저가 변경될때 유저 다 잡아서 바꿔버림

}
