package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyteam.RequestCompanyTeamRequest;
import com.gogetdata.company.application.dto.companyteam.RequestCompanyTeamResponse;
import com.gogetdata.company.application.dto.companyteam.UpdateTeamRequest;
import com.gogetdata.company.infrastructure.filter.CustomUserDetails;

import java.util.List;

public interface CompanyTeamService {
    MessageResponse requestCompanyTeam(CustomUserDetails userDetails, Long companyId, RequestCompanyTeamRequest requestCompanyTeamRequest); // 요청
    List<RequestCompanyTeamResponse> requestReadCompanyTeam(CustomUserDetails userDetails,Long companyId);    // 요청목록
    MessageResponse approveRequestCompanyTeam(CustomUserDetails userDetails,Long companyTeamId , Long companyId);     // 수락
    MessageResponse updateCompanyTeamName(CustomUserDetails userDetails, Long companyTeamId , Long companyId, UpdateTeamRequest updateTeamRequest);  // 이름변경
    MessageResponse deleteCompanyTeam(CustomUserDetails userDetails, Long companyTeamId , Long companyId);      // 삭제
    MessageResponse rejectRequestCompanyTeam(CustomUserDetails userDetails,Long companyTeamId,Long companyId);       // 거절
                                                                                                                // 회사 팀 조회

}




