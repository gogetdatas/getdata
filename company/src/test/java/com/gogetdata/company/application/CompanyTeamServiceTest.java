package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyteam.RequestCompanyTeamRequest;
import com.gogetdata.company.application.dto.companyteam.RequestCompanyTeamResponse;
import com.gogetdata.company.application.dto.companyteam.UpdateTeamRequest;
import com.gogetdata.company.domain.entity.*;
import com.gogetdata.company.domain.repository.companyteam.CompanyTeamRepository;
import com.gogetdata.company.domain.repository.companyteamuser.CompanyTeamUserRepository;
import com.gogetdata.company.domain.repository.companyuser.CompanyUserRepository;
import com.gogetdata.company.domain.service.CompanyTeamServiceImpl;
import com.gogetdata.company.infrastructure.filter.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)

class CompanyTeamServiceTest {
    @InjectMocks
    private CompanyTeamServiceImpl companyTeamService;
    @Mock
    private CompanyTeamRepository companyTeamRepository;
    @Mock
    private CompanyUserRepository companyUserRepository;
    @Mock
    private CompanyTeamUserRepository companyTeamUserRepository;
    private Long companyId;
    private CustomUserDetails customUserDetails;
    @BeforeEach
    void setUp() {
        this.companyId = 1L;
        this.customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")));
    }
    @Nested
    @DisplayName("업체팀생성 요청")
    class RequestCompanyTeam {
        @DisplayName("성공적으로 업체생성 요청")
        @Test
        void successRequestCompanyTeam() {
            // given
            RequestCompanyTeamRequest requestCompanyTeam = new RequestCompanyTeamRequest("team1");
            CompanyUser companyUser =  new CompanyUser(1L, companyId, 1L, AffiliationStatus.APPROVED, CompanyUserType.USER, "User1", "User1@email.com");
            given(companyUserRepository.isApprovalUser(customUserDetails.getUserId(), companyId)).willReturn(companyUser);

            // when
            MessageResponse result = companyTeamService.requestCompanyTeam(customUserDetails,1L,requestCompanyTeam);
            // then
            assertThat(result.getMessage()).isEqualTo("요청완료");
        }
    }
    @Nested
    @DisplayName("업체팀생성 요청 조회")
    class RequestReadCompanyTeam {
        @DisplayName("성공적으로 업체팀생성 요청 조회 성공")
        @Test
        void successRequestReadCompanyTeam() {
            // given
            CompanyUser companyUser = new CompanyUser(1L,companyId,1L,AffiliationStatus.APPROVED,CompanyUserType.ADMIN,"user1","user1@email.com");
            given(companyUserRepository.isApprovalUser(customUserDetails.getUserId(), companyId)).willReturn(companyUser);
            List<CompanyTeam> companyTeams = List.of(
                    new CompanyTeam(1L,companyId,"team1", CompanyTeamStatus.PENDING),
                    new CompanyTeam(2L,companyId,"team2", CompanyTeamStatus.PENDING),
                    new CompanyTeam(3L,companyId,"team3", CompanyTeamStatus.PENDING)
            );
            given(companyTeamRepository.readRequestCompanyTeams(companyId)).willReturn(companyTeams);

            List<RequestCompanyTeamResponse> companyTeamResponses= List.of(
                    new RequestCompanyTeamResponse(1L,"team1", CompanyTeamStatus.PENDING),
                    new RequestCompanyTeamResponse(2L,"team2", CompanyTeamStatus.PENDING),
                    new RequestCompanyTeamResponse(3L,"team3", CompanyTeamStatus.PENDING)
                    );
            // when
            List<RequestCompanyTeamResponse> results = companyTeamService.requestReadCompanyTeam(customUserDetails,companyId);
            // then
            assertThat(results).containsExactlyElementsOf(companyTeamResponses);
        }
    }
    @Nested
    @DisplayName("업체팀 생성 요청 수락")
    class ApproveRequestCompanyTeam {
        @DisplayName("성공적으로 업체팀생성 요청 조회 성공")
        @Test
        void successApproveRequestCompanyTeam() {
            // given
            CompanyUser loginUser = new CompanyUser(1L,companyId,1L,AffiliationStatus.APPROVED,CompanyUserType.ADMIN,"user1","user1@email.com");

            given(companyUserRepository.isApprovalUser(customUserDetails.getUserId(), companyId)).willReturn(loginUser);
            CompanyTeam companyTeam = new CompanyTeam(3L,companyId,"team3", CompanyTeamStatus.PENDING);
            given(companyTeamRepository.readRequestCompanyTeam(3L)).willReturn(companyTeam);
            CompanyUser companyUser =  new CompanyUser(2L,companyId,1L,AffiliationStatus.APPROVED,CompanyUserType.USER,
                    "user1","user1@email.com");
            given(companyUserRepository.isApprovalUser(companyId,companyTeam.getCreatedBy())).willReturn(companyUser);
            // when
            MessageResponse result = companyTeamService.approveRequestCompanyTeam(customUserDetails,3L,companyId);
            // then
            assertThat(result.getMessage()).isEqualTo("승인");
        }
    }
    @Nested
    @DisplayName("업체팀생성 거절")
    class RejectRequestCompanyTeam {
        @DisplayName("성공적으로 업체팀생성 요청 거절 성공")
        @Test
        void successRejectRequestCompanyTeam() {
            // given
            CompanyUser loginUser = new CompanyUser(1L,companyId,1L,AffiliationStatus.APPROVED,CompanyUserType.ADMIN,"user1","user1@email.com");

            given(companyUserRepository.isApprovalUser(customUserDetails.getUserId(), companyId)).willReturn(loginUser);
            CompanyTeam companyTeam = new CompanyTeam(3L,companyId,"team3", CompanyTeamStatus.PENDING);
            given(companyTeamRepository.readRequestCompanyTeam(3L)).willReturn(companyTeam);
            // when
            MessageResponse result = companyTeamService.rejectRequestCompanyTeam(customUserDetails,3L,companyId);
            // then
            assertThat(result.getMessage()).isEqualTo("거절");
        }
    }

    @Nested
    @DisplayName("업체팀삭제")
    class DeleteCompanyTeam {
        @DisplayName("성공적으로 업체팀 삭제 성공")
        @Test
        void successDeleteCompanyTeam() {
            // given
            CompanyUser loginUser = new CompanyUser(1L,companyId,1L,AffiliationStatus.APPROVED,CompanyUserType.ADMIN,"user1","user1@email.com");

            given(companyUserRepository.isApprovalUser(customUserDetails.getUserId(), companyId)).willReturn(loginUser);
            CompanyTeam companyTeam = new CompanyTeam(3L,companyId,"team3", CompanyTeamStatus.PENDING);
            given(companyTeamRepository.findById(companyTeam.getCompanyTeamId())).willReturn(Optional.of(companyTeam));
            // when
            MessageResponse result = companyTeamService.deleteCompanyTeam(customUserDetails,3L,companyId);
            // then
            assertThat(result.getMessage()).isEqualTo("삭제완료");
        }
    }
    @Nested
    @DisplayName("업체팀수정")
    class UpdateCompanyTeamName {
        @DisplayName("성공적으로 업체팀 이름 변경 성공")
        @Test
        void successUpdateCompanyTeamName() {
            // given
            CompanyUser loginUser = new CompanyUser(1L,companyId,1L,AffiliationStatus.APPROVED,CompanyUserType.ADMIN,"user1","user1@email.com");
            given(companyUserRepository.isApprovalUser(customUserDetails.getUserId(), companyId)).willReturn(loginUser);
            CompanyTeam companyTeam = new CompanyTeam(3L,companyId,"team3", CompanyTeamStatus.PENDING);
            given(companyTeamRepository.findById(companyTeam.getCompanyTeamId())).willReturn(Optional.of(companyTeam));
            UpdateTeamRequest updateTeamRequest = new UpdateTeamRequest("updateTeam1");
            // when
            MessageResponse result = companyTeamService.updateCompanyTeamName(customUserDetails,3L,companyId,updateTeamRequest);
            // then
            assertThat(result.getMessage()).isEqualTo("이름변경 변경이름 :" + companyTeam.getCompanyTeamName());
        }
    }
}