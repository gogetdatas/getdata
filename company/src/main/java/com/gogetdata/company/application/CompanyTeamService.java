package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyteam.RequestCompanyTeamRequest;
import com.gogetdata.company.application.dto.companyteam.RequestCompanyTeamResponse;
import com.gogetdata.company.application.dto.companyteam.UpdateTeamRequest;

import java.util.List;

public interface CompanyTeamService {
    MessageResponse requestCompanyTeam(Long loginUserId,String role,  Long companyId, RequestCompanyTeamRequest requestCompanyTeamRequest); // 요청
    List<RequestCompanyTeamResponse> requestReadCompanyTeam(Long loginUserId,String role ,Long companyId);    // 요청목록
    MessageResponse approveRequestCompanyTeam(Long loginUserId,String role ,Long companyTeamId , Long companyId);     // 수락
    MessageResponse updateCompanyTeamName(Long loginUserId,String role , Long companyTeamId , Long companyId, UpdateTeamRequest updateTeamRequest);  // 이름변경
    MessageResponse deleteCompanyTeam(Long loginUserId,String role , Long companyTeamId , Long companyId);      // 삭제
    MessageResponse rejectRequestCompanyTeam(Long loginUserId,String role ,Long companyTeamId,Long companyId);       // 거절
                                                                                                                // 회사 팀 조회

}




