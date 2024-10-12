package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyuser.*;
import com.gogetdata.company.application.dto.feignclient.MyInfoResponse;
import com.gogetdata.company.application.dto.feignclient.RegistrationResults;
import com.gogetdata.company.domain.entity.AffiliationStatus;
import com.gogetdata.company.domain.entity.CompanyUser;
import com.gogetdata.company.domain.entity.CompanyUserType;
import com.gogetdata.company.domain.repository.companyuser.CompanyUserRepository;
import com.gogetdata.company.domain.service.CompanyUserServiceImpl;
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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CompanyUserServiceTest {
    @InjectMocks
    private CompanyUserServiceImpl companyUserService;
    @Mock
    private CompanyUserRepository companyUserRepository;
    @Mock
    private UserService userService;
    private Long companyId;
    private Long companyUserId;
    private CompanyUser companyUser;

    @BeforeEach
    void setUp() {
        this.companyId = 1L;
        this.companyUserId = 1L;
        this.companyUser = CompanyUser.create(1L,1L,AffiliationStatus.APPROVED,CompanyUserType.USER,"user1","user1@email.com");
    }

    @Nested
    @DisplayName("업체유저수락")
    class RegisterCompanyUser {
        @DisplayName("성공적으로 업체유저 수락 성공")
        @Test
        void successRegisterCompanyUser() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            CompanyUser loginUser = new CompanyUser(1L,companyId,1L,AffiliationStatus.APPROVED,CompanyUserType.ADMIN,"user1","user1@email.com");
            given(companyUserRepository.isApprovalUser(customUserDetails.userId(), companyId)).willReturn(loginUser);
            List<UserRegistrationRequest> userRegistrationRequests = Arrays.asList(
                    new UserRegistrationRequest(1L, 1L, "ADMIN"),
                    new UserRegistrationRequest(2L, 2L, "USER")
            );
            List<RegistrationResults> registrationResults = Arrays.asList(
                    new RegistrationResults(1L, 1L, true, "ADMIN", "User1"),
                    new RegistrationResults(2L, 2L, false, "USER", "User2")
            );
            given(userService.registerUsers(userRegistrationRequests)).willReturn(registrationResults);
            List<CompanyUser> waitingUsers = List.of(
                    new CompanyUser(1L, companyId, 1L, AffiliationStatus.PENDING, CompanyUserType.UNASSIGN, "User1", "User1@email.com")
            );
            Map<Long, RegistrationResults> approvedResultsMap = registrationResults.stream()
                    .filter(RegistrationResults::isSuccess)
                    .collect(Collectors.toMap(RegistrationResults::getCompanyUserId, Function.identity()));
            given(companyUserRepository.selectWaitingForApprovalUsers(
                    approvedResultsMap.keySet().stream().toList(),
                    companyId
            )).willReturn(waitingUsers);

            // when
            List<CompanyUserRegistrationResponse> response = companyUserService.registerUserToCompany(
                    customUserDetails, userRegistrationRequests, companyId);
            // then
            assertThat(response).hasSize(1);
            assertThat(response.get(0).userName()).isEqualTo("User1");

            verify(companyUserRepository, times(1)).saveAll(waitingUsers);

            assertThat(waitingUsers).hasSize(1);
            for (CompanyUser companyUser : waitingUsers) {
                assertThat(companyUser.getStatus()).isEqualTo(AffiliationStatus.APPROVED);
                assertThat(companyUser.getType()).isEqualTo(CompanyUserType.ADMIN);
            }
        }
    }

    @Nested
    @DisplayName("업체유저삭제")
    class DeleteCompanyUser {
        @DisplayName("성공적으로 업체유저 삭제")
        @Test
        void successDeleteCompanyUser() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            CompanyUser loginUser = new CompanyUser(1L,companyId,1L,AffiliationStatus.APPROVED,CompanyUserType.ADMIN,"user1","user1@email.com");
            given(companyUserRepository.isApprovalUser(customUserDetails.userId(), companyId)).willReturn(loginUser);
            given(companyUserRepository.findById(companyUserId)).willReturn(Optional.of(companyUser));
            given(userService.deleteCompanyUser(companyUserId)).willReturn(true);
            // when
            MessageResponse result = companyUserService.deleteCompanyUser(customUserDetails,1L,1L);
            // then
            assertThat(result.getMessage()).isEqualTo("삭제완료");

        }
    }

    @Nested
    @DisplayName("업체유저권한수정")
    class UpdateCompanyUserType {
        @DisplayName("성공적으로 업체유저 권한 변경")
        @Test
        void successUpdateCompanyUserType() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            CompanyUser loginUser = new CompanyUser(1L,companyId,1L,AffiliationStatus.APPROVED,CompanyUserType.ADMIN,"user1","user1@email.com");
            given(companyUserRepository.isApprovalUser(customUserDetails.userId(), companyId)).willReturn(loginUser);
            given(companyUserRepository.findById(companyUserId)).willReturn(Optional.of(companyUser));
            UpdateCompanyUserTypeRequest updateCompanyUserTypeRequest = new UpdateCompanyUserTypeRequest("ADMIN");
            // when
            MessageResponse result = companyUserService.updateCompanyTypeUser(customUserDetails,companyUserId,companyId,updateCompanyUserTypeRequest);
            // then
            assertThat(result.getMessage()).isEqualTo("타입변경");

        }
    }
    @Nested
    @DisplayName("업체유저목록조회")
    class ReadsCompanyUser {
        @DisplayName("성공적으로 업체 유저 목록 반환")
        @Test
        void successReadsCompanyUsers() {
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            CompanyUser loginUser = new CompanyUser(1L,companyId,1L,AffiliationStatus.APPROVED,CompanyUserType.USER,"user1","user1@email.com");
            given(companyUserRepository.isApprovalUser(customUserDetails.userId(), companyId)).willReturn(loginUser);

            // given
            List<CompanyUser> companyUsers = List.of(
                    new CompanyUser(1L, companyId, 1L, AffiliationStatus.APPROVED, CompanyUserType.USER, "User1", "User1@email.com"),
                    new CompanyUser(2L, companyId, 2L, AffiliationStatus.APPROVED, CompanyUserType.ADMIN, "User1", "User1@email.com"),
                    new CompanyUser(3L, companyId, 3L, AffiliationStatus.APPROVED, CompanyUserType.USER, "User1", "User1@email.com")
            );
            given(companyUserRepository.ApprovalUsers(companyId)).willReturn(companyUsers);
            List<CompanyUserResponse> companyUserResponses = new ArrayList<>();
            for (CompanyUser user : companyUsers) {
                companyUserResponses.add(CompanyUserResponse.from(user));
            }
            // when
            List<CompanyUserResponse> results = companyUserService.readsCompanyUser(customUserDetails,companyId);
            // then
            assertThat(results).hasSize(3);

        }
    }
    @Nested
    @DisplayName("업체유저조회")
    class ReadCompanyUser {
        @DisplayName("성공적으로 업체 유저 반환")
        @Test
        void successReadCompanyUser() {
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            CompanyUser loginUser = new CompanyUser(1L,companyId,1L,AffiliationStatus.APPROVED,CompanyUserType.USER,"user1","user1@email.com");
            given(companyUserRepository.isApprovalUser(customUserDetails.userId(), companyId)).willReturn(loginUser);

            // given
            CompanyUser companyUser = new CompanyUser(1L, companyId, 1L, AffiliationStatus.APPROVED, CompanyUserType.USER, "User1", "User1@email.com");
            given(companyUserRepository.ApprovalUser(companyId,companyUserId)).willReturn(companyUser); // 이거 그냥 companyUserId만 넘겨줘도 되겠다
            CompanyUserResponse companyUserResponses = CompanyUserResponse.from(companyUser);
            // when
            CompanyUserResponse result = companyUserService.readCompanyUser(customUserDetails,companyId,companyUserId);
            // then
            assertThat(result.userId()).isEqualTo(1L);

        }
    }



    @Nested
    @DisplayName("업체유저요청")
    class RequestCompanyUser {
        @DisplayName("성공적으로 유저 업체 요청")
        @Test
        void requestCompanyUser() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            given(userService.checkUser(customUserDetails.userId())).willReturn(true);
            MyInfoResponse myInfo = new MyInfoResponse(customUserDetails.userId(),"user1","user1@email.com",false);
            given(userService.readUser(customUserDetails.userId())).willReturn(myInfo);

            CompanyUser companyUser = new CompanyUser(1L, companyId, customUserDetails.userId(), AffiliationStatus.PENDING, CompanyUserType.UNASSIGN, myInfo.getUserName(), myInfo.getEmail());
            // when
            MessageResponse result = companyUserService.requestCompanyUser(customUserDetails,companyId);
            // then
            assertThat(result.getMessage()).isEqualTo("요청완료");

        }
    }

    @Nested
    @DisplayName("업체유저요청거부")
    class RejectCompanyUser {
        @DisplayName("성공적으로 업체 유저 거부")
        @Test
        void rejectCompanyUser() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            CompanyUser loginUser = new CompanyUser(1L,companyId,1L,AffiliationStatus.APPROVED,CompanyUserType.ADMIN,"user1","user1@email.com");
            given(companyUserRepository.isApprovalUser(customUserDetails.userId(), companyId)).willReturn(loginUser);
            CompanyUser companyUser = new CompanyUser(1L, companyId, customUserDetails.userId(), AffiliationStatus.PENDING, CompanyUserType.UNASSIGN, "user1","user1@email.com");
            given(companyUserRepository.waitingForApprovalUser(companyId, companyUserId)).willReturn(companyUser);
            // when
            MessageResponse result = companyUserService.rejectCompanyUser( customUserDetails,companyId,companyUserId);
            // then
            assertThat(result.getMessage()).isEqualTo("거절완료");

        }
    }

    @Nested
    @DisplayName("업체유저요청목록조회")
    class ReadsRequestCompanyUser {
        @DisplayName("성공적으로 업체 요청 목록 조회")
        @Test
        void readsRequestCompanyUser() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            CompanyUser loginUser = new CompanyUser(1L,companyId,1L,AffiliationStatus.APPROVED,CompanyUserType.ADMIN,"user1","user1@email.com");
            given(companyUserRepository.isApprovalUser(customUserDetails.userId(), companyId)).willReturn(loginUser);
            List<CompanyWaitingUserResponse> companyUserResponses= List.of(
                    new CompanyWaitingUserResponse(2L,"user2","user2@email.com"),
                    new CompanyWaitingUserResponse(3L,"user3","user3@email.com"),
                    new CompanyWaitingUserResponse(4L,"user4","user4@email.com")
            );
            List<CompanyUser> companyUsers = List.of(
                    new CompanyUser(2L,companyId,2L,AffiliationStatus.PENDING,CompanyUserType.UNASSIGN,"user2","user2@email.com"),
                    new CompanyUser(3L,companyId,3L,AffiliationStatus.PENDING,CompanyUserType.UNASSIGN,"user3","user3@email.com"),
                    new CompanyUser(4L,companyId,4L,AffiliationStatus.PENDING,CompanyUserType.UNASSIGN,"user4","user4@email.com")
            );
            given(companyUserRepository.waitingForApprovalUsers(companyId)).willReturn(companyUsers);
            // when
            List<CompanyWaitingUserResponse> result = companyUserService.readsRequestCompanyUser(customUserDetails,companyId);
            // then
            assertThat(result).containsExactlyElementsOf(companyUserResponses);
        }
    }
}