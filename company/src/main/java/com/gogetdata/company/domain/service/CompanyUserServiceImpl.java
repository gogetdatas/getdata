package com.gogetdata.company.domain.service;

import com.gogetdata.company.application.CompanyUserService;
import com.gogetdata.company.application.UserService;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyuser.*;
import com.gogetdata.company.application.dto.feignclient.MyInfoResponse;
import com.gogetdata.company.application.dto.feignclient.RegistrationResult;
import com.gogetdata.company.application.dto.feignclient.RegistrationResults;
import com.gogetdata.company.domain.entity.AffiliationStatus;
import com.gogetdata.company.domain.entity.CompanyUser;
import com.gogetdata.company.domain.entity.CompanyUserType;
import com.gogetdata.company.domain.repository.companyuser.CompanyUserRepository;
import com.gogetdata.company.infrastructure.filter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyUserServiceImpl implements CompanyUserService {
    private final UserService userService;
    private final CompanyUserRepository companyUserRepository;

    /**
     * 사용자를 회사에 등록합니다.
     *
     * @param customUserDetails      현재 사용자 정보
     * @param userRegistrationRequests 사용자 등록 요청 목록
     * @param companyId                대상 회사 ID
     * @return 등록된 사용자 응답 목록
     */
    @Override
    @Transactional
    public List<CompanyUserRegistrationResponse> registerUserToCompany(CustomUserDetails customUserDetails,
                                                                       List<UserRegistrationRequest> userRegistrationRequests,
                                                                       Long companyId) {
        authorizeAdminOrCompanyAdmin(customUserDetails,companyId);

        UserRegistration userRegistration = new UserRegistration(companyId,userRegistrationRequests);
        List<RegistrationResults> results = userService.registerUsers(userRegistration);

        Map<Long, RegistrationResults> approvedResultsMap = results.stream()
                .filter(RegistrationResults::getIsSuccess)
                .collect(Collectors.toMap(RegistrationResults::getCompanyUserId, Function.identity()));

        if (approvedResultsMap.isEmpty()) {
            return Collections.emptyList();
        }

        List<CompanyUser> companyUsers = companyUserRepository.selectWaitingForApprovalUsers(approvedResultsMap.keySet().stream().toList(), companyId);
        List<CompanyUserRegistrationResponse> registrationResponses = new ArrayList<>();

        for (CompanyUser companyUser : companyUsers) {
            RegistrationResults result = approvedResultsMap.get(companyUser.getCompanyUserId());
            if (result != null) {
                companyUser.registerUserToCompany(AffiliationStatus.APPROVED, CompanyUserType.valueOf(result.getType()));
                registrationResponses.add(CompanyUserRegistrationResponse.from(result.getUserName()));
            }
        }

        companyUserRepository.saveAll(companyUsers);
        return registrationResponses;
    }

    /**
     * 회사 사용자 삭제 메서드.
     *
     * @param customUserDetails 현재 사용자 정보
     * @param companyUserId     삭제하려는 회사 사용자 ID
     * @param companyId         대상 회사 ID
     * @return 삭제 성공 메시지
     */
    @Override
    @Transactional
    public MessageResponse deleteCompanyUser(CustomUserDetails customUserDetails, Long companyUserId, Long companyId) {
        authorizeAdminOrCompanyAdminOrSelf(customUserDetails,companyUserId,companyId);

        CompanyUser companyUser = validateCompanyUserNotDeleted(isExistCompanyUser(companyUserId));
        if (userService.deleteCompanyUser(companyUser.getUserId())) {
            companyUser.Delete();
            companyUserRepository.save(companyUser);
            return MessageResponse.from("삭제완료");
        }
        throw new IllegalArgumentException("삭제실패");
    }

    /**
     * 회사 사용자 타입을 업데이트합니다.
     *
     * @param customUserDetails        현재 사용자 정보
     * @param companyUserId            업데이트할 회사 사용자 ID
     * @param companyId                대상 회사 ID
     * @param updateCompanyTypeRequest 타입 업데이트 요청
     * @return 업데이트 성공 메시지
     */
    @Override
    @Transactional
    public MessageResponse updateCompanyTypeUser(CustomUserDetails customUserDetails, Long companyUserId, Long companyId, UpdateCompanyUserTypeRequest updateCompanyTypeRequest) {
        authorizeAdminOrCompanyAdmin(customUserDetails,companyId);

        CompanyUser companyUser = validateCompanyUserNotDeleted(isExistCompanyUser(companyUserId));
        companyUser.updateTypeUserCompany(CompanyUserType.valueOf(updateCompanyTypeRequest.getType()));
        companyUserRepository.save(companyUser);
        return MessageResponse.from("타입변경");
    }

    /**
     * 회사 내 모든 사용자를 조회합니다.
     *
     * @param customUserDetails 현재 사용자 정보
     * @param companyId         대상 회사 ID
     * @return 회사 사용자 목록
     */
    @Override
    public List<CompanyUserResponse> readsCompanyUser(CustomUserDetails customUserDetails, Long companyId) {
        validateUserAffiliation(customUserDetails,companyId);

        List<CompanyUser> companyUsers = companyUserRepository.ApprovalUsers(companyId);
        List<CompanyUserResponse> companyUserResponses = new ArrayList<>();
        for (CompanyUser companyUser : companyUsers) {
            companyUserResponses.add(CompanyUserResponse.from(companyUser));
        }
        return companyUserResponses;
    }
    /**
     * 특정 회사 사용자를 조회합니다.
     *
     * @param customUserDetails 현재 사용자 정보
     * @param companyId         대상 회사 ID
     * @param companyUserId     조회할 회사 사용자 ID
     * @return 조회된 회사 사용자 정보
     */

    @Override
    public CompanyUserResponse readCompanyUser(CustomUserDetails customUserDetails, Long companyId, Long companyUserId) {
        validateUserAffiliation(customUserDetails,companyId);

        CompanyUser companyUser = companyUserRepository.ApprovalUser(companyId, companyUserId);
        return CompanyUserResponse.from(companyUser);
    }

    /**
     * 회사 가입 요청을 합니다.
     *
     * @param customUserDetails 현재 사용자 정보
     * @param companyId         대상 회사 ID
     * @return 요청 성공 메시지
     */
    @Override
    public MessageResponse requestCompanyUser(CustomUserDetails customUserDetails, Long companyId) { // 이거 여러번 요청가능
        RegistrationResult result = userService.checkUser(customUserDetails.userId());
        if (result.getIsSuccess()) {
            companyUserRepository.save(CompanyUser.create(companyId,
                    customUserDetails.userId(),
                    AffiliationStatus.PENDING,
                    CompanyUserType.UNASSIGN,
                    result.getUserName(),
                    result.getEmail()));
            return MessageResponse.from("요청완료");
        }
        throw new IllegalArgumentException("이미 회사에 소속되어 있음");
    }
    /**
     * 회사 사용자 요청을 거절합니다.
     *
     * @param customUserDetails 현재 사용자 정보
     * @param companyId         대상 회사 ID
     * @param companyUserId     거절할 회사 사용자 ID
     * @return 거절 성공 메시지
     */
    @Override
    public MessageResponse rejectCompanyUser(CustomUserDetails customUserDetails, Long companyId, Long companyUserId) {
        authorizeAdminOrCompanyAdmin(customUserDetails,companyId);

        CompanyUser companyUser = companyUserRepository.waitingForApprovalUser(companyId, companyUserId);
        companyUser.updateStatusUserCompany(AffiliationStatus.REJECTED);
        companyUserRepository.save(companyUser);
        return MessageResponse.from("거절완료");
    }

    /**
     * 대기 중인 회사 사용자 요청을 조회합니다.
     *
     * @param customUserDetails 현재 사용자 정보
     * @param companyId         대상 회사 ID
     * @return 대기 중인 사용자 목록
     */

    @Override
    public List<CompanyWaitingUserResponse> readsRequestCompanyUser(CustomUserDetails customUserDetails, Long companyId) {
        authorizeAdminOrCompanyAdmin(customUserDetails,companyId);

        List<CompanyWaitingUserResponse> companyUserResponses = new ArrayList<>();
        List<CompanyUser> companyUsers = companyUserRepository.waitingForApprovalUsers(companyId);
        for (CompanyUser companyUser : companyUsers) {
            companyUserResponses.add(CompanyWaitingUserResponse.from(companyUser));
        }
        return companyUserResponses;
    }

    @Override
    public List<CompanyUserResponse> searchCompanyUser(CustomUserDetails customUserDetails, Long companyId,String companyUserName) {
        validateUserAffiliation(customUserDetails,companyId);
        List<CompanyUserResponse> companyUserResponses = new ArrayList<>();
        List<CompanyUser> companyUsers = companyUserRepository.getSearchApprovalUsers(companyId,companyUserName);
        for (CompanyUser companyUser : companyUsers) {
            companyUserResponses.add(CompanyUserResponse.from(companyUser));
        }
        return companyUserResponses;
    }


    private boolean isAdmin(String role) {
        return role.equals("ADMIN");
    }
    private void authorizeAdminOrCompanyAdmin(CustomUserDetails customUserDetails, Long companyId) {
        if (isAdmin(customUserDetails.getAuthorities().toString())) {
            return;
        }

        CompanyUser loginUser = companyUserRepository.isApprovalUser(companyId, customUserDetails.userId());
        if (loginUser == null || !loginUser.getType().getAuthority().equals("ADMIN")) {
            throw new IllegalArgumentException("권한없음");
        }
    }
    private void authorizeAdminOrCompanyAdminOrSelf(CustomUserDetails customUserDetails, Long companyUserId, Long companyId) {
        if (isAdmin(customUserDetails.getAuthorities().toString())) {
            return;
        }

        CompanyUser loginUser = companyUserRepository.isApprovalUser(companyId, customUserDetails.userId());
        if (loginUser == null) {
            throw new IllegalArgumentException("권한없음");
        }

        boolean isAdmin = "ADMIN".equals(loginUser.getType().getAuthority());
        boolean isSelf = Objects.equals(loginUser.getCompanyUserId(), companyUserId);

        if (!isAdmin && !isSelf) {
            throw new IllegalArgumentException("권한없음");
        }
    }
    private void validateUserAffiliation(CustomUserDetails customUserDetails , Long companyId) {
        if (isAdmin(customUserDetails.getAuthorities().toString())) {
            return;
        }
        CompanyUser loginUser = companyUserRepository.isApprovalUser(companyId, customUserDetails.userId());
        if (loginUser == null) {
            throw new IllegalArgumentException("권한없음");
        }
    }
    public CompanyUser isExistCompanyUser(Long companyUserId) {
        return companyUserRepository.findById(companyUserId)
                .orElseThrow(() -> new NoSuchElementException("No user found with id " + companyUserId));
    }
    public CompanyUser validateCompanyUserNotDeleted(CompanyUser companyUser) {
        if (companyUser.isDeleted()) {
            throw new NoSuchElementException("User with id " + companyUser.getUserId() + " is deleted.");
        }
        return companyUser;
    }
}
