package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyteam.CompanyTeamResponse;
import com.gogetdata.company.application.dto.companyteamuser.AcceptJoinRequest;
import com.gogetdata.company.application.dto.companyteamuser.CompanyTeamUserResponse;
import com.gogetdata.company.application.dto.companyteamuser.UpdateUserPerMissionRequest;
import com.gogetdata.company.domain.entity.*;
import com.gogetdata.company.domain.repository.companyteamuser.CompanyTeamUserRepository;
import com.gogetdata.company.domain.repository.companyuser.CompanyUserRepository;
import com.gogetdata.company.domain.service.CompanyTeamUserServiceImpl;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
class CompanyTeamUserServiceTest {
    @InjectMocks
    private CompanyTeamUserServiceImpl companyTeamUserService;
    @Mock
    private CompanyUserRepository companyUserRepository;
    @Mock
    private CompanyTeamUserRepository companyTeamUserRepository;
    private Long companyId;
    private Long companyTeamId;
    private Long userId;

    @BeforeEach
    void setUp() {
        this.companyId = 1L;
        userId = 1L;
        companyId = 100L;
        companyTeamId = 200L;
    }
    @Nested
    @DisplayName("업체팀유저요청")
    class ApplyToJoinTeam {
        @DisplayName("성공적으로 업체생성 요청")
        @Test
        void successApplyToJoinTeam() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            CompanyUser loginUser = new CompanyUser(companyId,customUserDetails.userId(),1L,AffiliationStatus.APPROVED,CompanyUserType.USER,"user1","user1@email.com");
            given(companyUserRepository.isApprovalUser(companyId,customUserDetails.userId())).willReturn(loginUser);
            // when
            MessageResponse result = companyTeamUserService.applyToJoinTeam(customUserDetails,companyId,companyTeamId);
            // then
            assertThat(result.getMessage()).isEqualTo("요청완료");
        }
    }
    @Nested
    @DisplayName("업체팀유저요청 수락")
    class acceptJoinRequest {
        @DisplayName("성공적으로 업체팀유저 요청 수락")
        @Test
        void successAcceptJoinRequest() {
            //given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            List<AcceptJoinRequest> acceptJoinRequest = Arrays.asList(
                    new AcceptJoinRequest(10L, "ADMIN"),
                    new AcceptJoinRequest(11L, "USER")
            );

            given(companyTeamUserRepository.isExistAdminUser(companyTeamId, userId)).willReturn(true);
            List<CompanyTeamUser> existingUsers = Arrays.asList(
                    new CompanyTeamUser(10L,companyTeamId,2L,"User2","user2@email.com",
                            CompanyTeamUserStatus.PENDING,CompanyTeamUserType.UNASSIGN),
                    new CompanyTeamUser(11L,companyTeamId,3L,"User3","user3@email.com",
                            CompanyTeamUserStatus.PENDING,CompanyTeamUserType.UNASSIGN)
                    );

            given(companyTeamUserRepository.isExistUsers(companyId, companyTeamId, Arrays.asList(10L, 11L)))
                    .willReturn(existingUsers);

            // when
            List<MessageResponse> messages = companyTeamUserService.acceptJoinRequest(customUserDetails, companyId, companyTeamId, acceptJoinRequest);
            // then
            assertThat(messages).hasSize(2);
            assertThat(messages).extracting("message").containsExactlyInAnyOrder("User2등록", "User3등록");
        }
//        @Test
//        void acceptJoinRequest_asNonAdminAndNotAffiliated_throwsException() {
//            // Given
//            List<AcceptJoinRequest> acceptJoinRequest = Arrays.asList(
//                    new AcceptJoinRequest(10L, "ADMIN")
//            );
//
//            // Mock 권한 검사 실패
//            given(companyTeamUserRepository.isAuthorizedUser(companyTeamId, userId)).willReturn(false);
//
//            // When & Then
//            assertThatThrownBy(() ->
//                    companyTeamUserService.acceptJoinRequest(customUserDetails, companyId, companyTeamId, acceptJoinRequest)
//            )
//                    .isInstanceOf(UnauthorizedException.class)
//                    .hasMessage("권한없음: 팀 소속이 아니거나 관리자 권한이 없습니다.");
//
//            // saveAll이 호출되지 않았는지 검증
//            then(companyTeamUserRepository).should(never()).saveAll(anyList());
//        }
//
//        @Test
//        void acceptJoinRequest_asAdmin_withInvalidUserIds_skipsInvalidUsers() {
//            // Given
//            List<AcceptJoinRequest> acceptJoinRequest = Arrays.asList(
//                    new AcceptJoinRequest(10L, "ADMIN"),
//                    new AcceptJoinRequest(99L, "USER") // 존재하지 않는 ID
//            );
//
//            // Mock 권한 검사 성공
//            given(companyTeamUserRepository.isAuthorizedUser(companyTeamId, userId)).willReturn(true);
//
//            // Mock isExistUsers 호출 시 반환될 팀 유저 리스트 (only ID 10 exists)
//            List<CompanyTeamUser> existingUsers = Collections.singletonList(
//                    CompanyTeamUser.builder()
//                            .companyTeamUserId(10L)
//                            .companyTeamId(companyTeamId)
//                            .userId(2L)
//                            .userName("User2")
//                            .email("user2@example.com")
//                            .companyTeamUserStatus(CompanyTeamUserStatus.PENDING)
//                            .companyTeamUserType(CompanyTeamUserType.ADMIN)
//                            .build()
//            );
//            given(companyTeamUserRepository.isExistUsers(companyId, companyTeamId, Arrays.asList(10L, 99L)))
//                    .willReturn(existingUsers);
//
//            // When
//            List<MessageResponse> messages = companyTeamUserService.acceptJoinRequest(customUserDetails, companyId, companyTeamId, acceptJoinRequest);
//
//            // Then
//            assertThat(messages).hasSize(1);
//            assertThat(messages).extracting("message").containsExactly("User2등록");
//
//            // saveAll이 호출되었는지 검증
//            then(companyTeamUserRepository).should(times(1)).saveAll(existingUsers);
//        }
//
//        @Test
//        void acceptJoinRequest_asAdmin_withEmptyRequestList_returnsEmptyMessage() {
//            // Given
//            List<AcceptJoinRequest> acceptJoinRequest = Collections.emptyList();
//
//            // Mock 권한 검사 성공
//            given(companyTeamUserRepository.isAuthorizedUser(companyTeamId, userId)).willReturn(true);
//
//            // When
//            List<MessageResponse> messages = companyTeamUserService.acceptJoinRequest(customUserDetails, companyId, companyTeamId, acceptJoinRequest);
//
//            // Then
//            assertThat(messages).isEmpty();
//
//            // saveAll이 호출되지 않았는지 검증
//            then(companyTeamUserRepository).should(never()).saveAll(anyList());
//        }
    }
    @Nested
    @DisplayName("업체팀유저요청거부")
    class RejectJoinRequest {
        @DisplayName("성공적으로 업체팀 유저 요청 거부")
        @Test
        void successRejectJoinRequest() {
            //given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            given(companyTeamUserRepository.isExistAdminUser(companyTeamId, customUserDetails.userId())).willReturn(true);
            CompanyTeamUser companyTeamUser = new CompanyTeamUser(10L, companyTeamId, 10L, "User2", "user2@email.com",
                    CompanyTeamUserStatus.PENDING, CompanyTeamUserType.UNASSIGN);
            given(companyTeamUserRepository.isExistUser(companyId, companyTeamId, companyTeamUser.getCompanyTeamUserId()))
                    .willReturn(companyTeamUser);

            //when
            MessageResponse messages = companyTeamUserService.rejectJoinRequest(customUserDetails, companyId, companyTeamId, 10L);
            //then
            assertThat(messages.getMessage()).isEqualTo("거절");
        }

        @Nested
        @DisplayName("업체팀유저삭제")
        class DeleteUserFromTeam {
            @DisplayName("성공적으로 업체팀유저삭제")
            @Test
            void successDeleteUserFromTeam() {
                //given
                CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

                given(companyTeamUserRepository.isExistAdminUser(companyTeamId, customUserDetails.userId())).willReturn(true);
                CompanyTeamUser companyTeamUser = new CompanyTeamUser(10L, companyTeamId, 10L, "User2", "user2@email.com",
                        CompanyTeamUserStatus.APPROVED, CompanyTeamUserType.USER);
                given(companyTeamUserRepository.findById(10L)).willReturn(Optional.of(companyTeamUser));
                //when
                MessageResponse messages = companyTeamUserService.deleteUserFromTeam(customUserDetails, companyId, companyTeamId, 10L);
                //then
                assertThat(messages.getMessage()).isEqualTo("삭제");
            }
        }

        @Nested
        @DisplayName("업체팀유저권한수정")
        class UpdateUserPermission {
            @DisplayName("성공적으로 업체팀 유저 권한수정")
            @Test
            void successUpdateUserPermission() { //
                //given
                CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

                given(companyTeamUserRepository.isExistAdminUser(companyTeamId, customUserDetails.userId())).willReturn(true);
                CompanyTeamUser companyTeamUser = new CompanyTeamUser(10L, companyTeamId, 10L, "User2", "user2@email.com",
                        CompanyTeamUserStatus.APPROVED, CompanyTeamUserType.USER);
                UpdateUserPerMissionRequest updateUserPerMissionRequest = new UpdateUserPerMissionRequest(1L,"ADMIN");
                given(companyTeamUserRepository.isExistUser(companyId, companyTeamId, companyTeamUser.getCompanyTeamUserId()))
                        .willReturn(companyTeamUser);

                //when
                MessageResponse messages = companyTeamUserService.updateUserPermission(customUserDetails, companyId, companyTeamId, 10L,updateUserPerMissionRequest);
                //then
                assertThat(messages.getMessage()).isEqualTo("변경");
            }
        }

        @Nested
        @DisplayName("업체 유저 소속팀목록조회")
        class GetMyTeams {
            @DisplayName("성공적으로 업체 유저 소속팀목록조회")
            @Test
            void successGetMyTeams() {
                //given
                CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

                List<CompanyTeam> companyTeams = List.of(
                        new CompanyTeam(1L,companyId,"team1", CompanyTeamStatus.APPROVED),
                        new CompanyTeam(2L,companyId,"team2", CompanyTeamStatus.APPROVED)
                );
                given(companyTeamUserRepository.getMyTeams(customUserDetails.userId()))
                        .willReturn(companyTeams);

                List<CompanyTeamResponse> companyTeamResponses= List.of(
                        new CompanyTeamResponse(1L,"team1"),
                        new CompanyTeamResponse(2L,"team2")
                );
                //when
                List<CompanyTeamResponse> results = companyTeamUserService.getMyTeams(customUserDetails);
                //then
                assertThat(results).containsExactlyElementsOf(companyTeamResponses);
            }

        }

        @Nested
        @DisplayName("업체 팀 소속 유저 조회")
        class GetUsersInTeam {
            @DisplayName("성공적으로 업체 팀 소속 유저 조회")
            @Test
            void successGetUsersInTeam() {
                //given
                CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

                given(companyTeamUserRepository.isExistAdminOrUser(companyTeamId, customUserDetails.userId())).willReturn(true);
                List<CompanyTeamUser> companyTeamUsers = List.of(
                        new CompanyTeamUser(1L,companyTeamId,1L,"user1","user1@email.com",CompanyTeamUserStatus.APPROVED,CompanyTeamUserType.USER),
                        new CompanyTeamUser(2L,companyTeamId,2L,"user2","user2@email.com",CompanyTeamUserStatus.APPROVED,CompanyTeamUserType.USER)
                );
                given(companyTeamUserRepository.isExistUsers(companyTeamId))
                        .willReturn(companyTeamUsers);
                List<CompanyTeamUserResponse> companyTeamUserResponses= List.of(
                        new CompanyTeamUserResponse(1L,"user1","user1@email.com",CompanyTeamUserType.USER),
                        new CompanyTeamUserResponse(2L,"user2","user2@email.com",CompanyTeamUserType.USER)
                );
                //when
                List<CompanyTeamUserResponse> results = companyTeamUserService.getUsersInTeam(customUserDetails,companyTeamId);
                //then
                assertThat(results).containsExactlyElementsOf(companyTeamUserResponses);
            }
        }
    }
}