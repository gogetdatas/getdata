package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.company.CompanyResponse;
import com.gogetdata.company.application.dto.company.CreateCompanyRequest;
import com.gogetdata.company.application.dto.company.UpdateCompanyRequest;
import com.gogetdata.company.application.dto.feignclient.RegistrationResult;
import com.gogetdata.company.domain.entity.Company;
import com.gogetdata.company.domain.entity.CompanyUser;
import com.gogetdata.company.domain.repository.company.CompanyRepository;
import com.gogetdata.company.domain.repository.companyuser.CompanyUserRepository;
import com.gogetdata.company.domain.service.CompanyServiceImpl;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {
    @InjectMocks
    private CompanyServiceImpl companyService;
    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyUserRepository companyUserRepository;
    @Mock
    private UserService userService;
    private Long companyId;
    private Long userId;
    private Company company;

    @BeforeEach
    void setUp() {
        this.companyId = 1L;
        this.company = Company.create("abc", "@@@@@@@aldawldkawdwq");
        ReflectionTestUtils.setField(company, "companyId", this.companyId);
        this.userId = 1L;
    }

    @Nested
    @DisplayName("업체등록")
    class CreateCompany {
        @DisplayName("성공적으로 업체 등록 성공")
        @Test
        void successCreateCompany() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("Test Company");
            RegistrationResult result = new RegistrationResult(userId, "abc", "testuser@example.com", true);
            Company mockCompany = Company.create(createCompanyRequest.getCompanyName(), UUID.randomUUID().toString());

            when(companyRepository.save(any(Company.class))).thenReturn(mockCompany);
            when(userService.registerUser(userId)).thenReturn(result);
            // when
            CompanyResponse response = companyService.createCompany(customUserDetails, createCompanyRequest);

            // then
            verify(companyRepository, times(1)).save(any(Company.class));
            verify(userService, times(1)).registerUser(userId);
            verify(companyUserRepository, times(1)).save(any(CompanyUser.class));
            assertThat(response).isNotNull();
            assertThat(response.companyId()).isEqualTo(mockCompany.getCompanyId());
            assertThat(response.companyId()).isEqualTo(mockCompany.getCompanyId());
            assertThat(response.companyName()).isEqualTo("Test Company");
        }

        @DisplayName("업체 등록 성공실패 : 업체가 이미 소속돼어 있을때")
        @Test
        void FailCreateCompany() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("Test Company");
            RegistrationResult result = new RegistrationResult(1L, "abc", "testuser@example.com", false);
            when(userService.registerUser(userId)).thenReturn(result);
            // when
            Throwable throwable = catchThrowable(() -> companyService.createCompany(customUserDetails, createCompanyRequest));
            // then
            assertThat(throwable).isInstanceOf(IllegalAccessError.class);

        }
    }

    @Nested
    @DisplayName("업체조회")
    class ReadCompany {
        @DisplayName("업체 조회 : 성공적으로 조회")
        @Test
        void SuccessReadCompany() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            Company userCompany = new Company(1L,"abc","@@@@@@@aldawldkawdwq");
            given(companyUserRepository.getCompanyUser(userId, companyId)).willReturn(userCompany);
            // when
            CompanyResponse response = companyService.readCompany(customUserDetails, companyId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.companyId()).isEqualTo(1L);
            assertThat(response.companyName()).isEqualTo("abc");
            assertThat(response.companyKey()).isEqualTo("@@@@@@@aldawldkawdwq");
        }
    }

    @Nested
    @DisplayName("업체목록조회")
    class readsCompany {

    }

    @Nested
    @DisplayName("업체수정")
    class UpdateCompanyRequestTest {
        @DisplayName("업체 수정 : 성공적으로 수정")
        @Test
        void SuccessUpdateCompany() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"ADMIN");

            Company userCompany = new Company(1L,"abc","@@@@@@@aldawldkawdwq");
            given(companyUserRepository.getCompanyUserAdmin(userId, companyId)).willReturn(userCompany);
            UpdateCompanyRequest updateCompanyRequest = new UpdateCompanyRequest("acbd");

            // when
            CompanyResponse result = companyService.updateCompany(customUserDetails, companyId, updateCompanyRequest);
            // then
            assertThat(result.companyName()).isEqualTo("acbd");

        }

        @DisplayName("업체 수정 : 업체에 소속한 일반 유저가 수정")
        @Test
        void failUpdateCompany() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            given(companyUserRepository.getCompanyUserAdmin(userId, companyId)).willReturn(null);

            UpdateCompanyRequest updateCompanyRequest = new UpdateCompanyRequest("acbd");
            // when
            Throwable throwable = catchThrowable(() -> companyService.updateCompany(customUserDetails, companyId, updateCompanyRequest));

            // then
            assertThat(throwable).isInstanceOf(IllegalAccessError.class);

        }
    }

    @Nested
    @DisplayName("업체삭제")
    class DeleteCompanyTest {
        @DisplayName("업체 삭제 : 성공적으로 삭제")
        @Test
        void SuccessDeleteCompany() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"ADMIN");

            Company userCompany = new Company(1L,"abc","@@@@@@@aldawldkawdwq");

            given(companyUserRepository.getCompanyUserAdmin(userId, companyId)).willReturn(userCompany);
            // when
            MessageResponse result = companyService.deleteCompany(customUserDetails,companyId);
            // then
            assertThat(result.getMessage()).isEqualTo("삭제 완료되었습니다.");
        }

        @DisplayName("업체 삭제 : 삭제 실패 ")
        @Test
        void FailDeleteCompany() {
            // given
            CustomUserDetails customUserDetails = new CustomUserDetails(1L, Collections.singleton(new SimpleGrantedAuthority("USER")),1L,"USER");

            given(companyUserRepository.getCompanyUserAdmin(userId, companyId)).willReturn(null);
            // when
            Throwable throwable = catchThrowable(() -> companyService.deleteCompany(customUserDetails,companyId));
            // then
            assertThat(throwable).isInstanceOf(IllegalAccessError.class);
        }
    }

}