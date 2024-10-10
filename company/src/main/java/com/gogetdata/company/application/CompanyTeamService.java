package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyteam.RequestCompanyTeamRequest;
import com.gogetdata.company.application.dto.companyteam.RequestCompanyTeamResponse;
import com.gogetdata.company.application.dto.companyteam.UpdateTeamRequest;

import java.util.List;

public interface CompanyTeamService {
    MessageResponse requestCompanyTeam(Long loginUserId,String role,  Long companyId, RequestCompanyTeamRequest requestCompanyTeamRequest,Long loginCompanyId); // 요청
    List<RequestCompanyTeamResponse> requestReadCompanyTeam(String role ,Long companyId,Long loginCompanyId,String loginUserType);    // 요청목록
    MessageResponse approveRequestCompanyTeam(String role ,Long companyTeamId , Long companyId,Long loginCompanyId,String loginUserType);     // 수락
    MessageResponse updateCompanyTeamName(String role , Long companyTeamId , Long companyId, UpdateTeamRequest updateTeamRequest,Long loginCompanyId,String loginUserType);  // 이름변경
    MessageResponse deleteCompanyTeam(String role , Long companyTeamId , Long companyId,Long loginCompanyId,String loginUserType);      // 삭제
    MessageResponse rejectRequestCompanyTeam(String role ,Long companyTeamId,Long companyId,Long loginCompanyId,String loginUserType);       // 거절
                                                                                                                // 회사 팀 조회

}




