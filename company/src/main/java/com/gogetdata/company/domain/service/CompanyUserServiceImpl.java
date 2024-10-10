package com.gogetdata.company.domain.service;

import com.gogetdata.company.application.CompanyUserService;
import com.gogetdata.company.application.UserService;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyuser.*;
import com.gogetdata.company.application.dto.feignclient.RegistrationResult;
import com.gogetdata.company.application.dto.feignclient.RegistrationResults;
import com.gogetdata.company.domain.entity.AffiliationStatus;
import com.gogetdata.company.domain.entity.CompanyUser;
import com.gogetdata.company.domain.entity.CompanyUserType;
import com.gogetdata.company.domain.repository.companyuser.CompanyUserRepository;
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
     * @param role      현재 사용자 정보
     * @param userRegistrationRequests 사용자 등록 요청 목록
     * @param companyId                대상 회사 ID
     * @return 등록된 사용자 응답 목록
     */
    @Override
    @Transactional
    public List<CompanyUserRegistrationResponse> registerUserToCompany(String role,
                                                                       List<UserRegistrationRequest> userRegistrationRequests,
                                                                       Long companyId,Long loginCompanyId , String loginCompanyType) {
        getAdminAccessibleCompany(role,companyId,loginCompanyId,loginCompanyType);

        UserRegistration userRegistration = new UserRegistration(companyId,userRegistrationRequests);
        List<RegistrationResults> results = userService.registerUsers(userRegistration);
        Map<Long, RegistrationResults> approvedResultsMap = results.stream()
                .filter(RegistrationResults::isSuccess)
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
     * @param loginUserId,role 현재 사용자 정보
     * @param companyUserId     삭제하려는 회사 사용자 ID
     * @param companyId         대상 회사 ID
     * @return 삭제 성공 메시지
     */
    @Override
    @Transactional
    public MessageResponse deleteCompanyUser(Long loginUserId,String role, Long companyUserId, Long companyId,Long loginCompanyId , String loginCompanyType) {
        CompanyUser companyUser = validateCompanyUserNotDeleted(isExistCompanyUser(companyUserId));
        if(!companyUser.getUserId().equals(loginUserId)){
            getAdminAccessibleCompany(role,companyId,loginCompanyId,loginCompanyType);
        }
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
     * @param role        현재 사용자 정보
     * @param companyUserId            업데이트할 회사 사용자 ID
     * @param companyId                대상 회사 ID
     * @param updateCompanyTypeRequest 타입 업데이트 요청
     * @return 업데이트 성공 메시지
     */
    @Override
    @Transactional
    public MessageResponse updateCompanyTypeUser(String role, Long companyUserId, Long companyId,Long loginCompanyId ,
                                                 String loginCompanyType, UpdateCompanyUserTypeRequest updateCompanyTypeRequest) {
        getAdminAccessibleCompany(role,companyId,loginCompanyId,loginCompanyType);

        CompanyUser companyUser = validateCompanyUserNotDeleted(isExistCompanyUser(companyUserId));
        companyUser.updateTypeUserCompany(CompanyUserType.valueOf(updateCompanyTypeRequest.getType()));
        companyUserRepository.save(companyUser);
        return MessageResponse.from("타입변경");
    }

    /**
     * 회사 내 모든 사용자를 조회합니다.
     *
     * @param role 현재 사용자 정보
     * @param companyId         대상 회사 ID
     * @return 회사 사용자 목록
     */
    @Override
    public List<CompanyUserResponse> readsCompanyUser(String role, Long companyId , Long loginCompanyId) {
        getReadableCompany(role,loginCompanyId,companyId);

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
     * @param role 현재 사용자 정보
     * @param companyId         대상 회사 ID
     * @param companyUserId     조회할 회사 사용자 ID
     * @return 조회된 회사 사용자 정보
     */

    @Override
    public CompanyUserResponse readCompanyUser(String role, Long companyId, Long companyUserId,Long loginCompanyId) {
        getReadableCompany(role,loginCompanyId,companyId);

        CompanyUser companyUser = companyUserRepository.ApprovalUser(companyId, companyUserId);
        return CompanyUserResponse.from(companyUser);
    }

    /**
     * 회사 가입 요청을 합니다.
     *
     * @param loginUserId,role 현재 사용자 정보
     * @param companyId         대상 회사 ID
     * @return 요청 성공 메시지
     */
    @Override
    public MessageResponse requestCompanyUser(Long loginUserId,String role, Long companyId) {
        RegistrationResult result = userService.checkUser(loginUserId);
        if (result.isSuccess()) {
            companyUserRepository.save(CompanyUser.create(companyId,
                    loginUserId,
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
     * @param role 현재 사용자 정보
     * @param companyId         대상 회사 ID
     * @param companyUserId     거절할 회사 사용자 ID
     * @return 거절 성공 메시지
     */
    @Override
    public MessageResponse rejectCompanyUser(String role, Long companyId, Long companyUserId , Long loginCompanyId ,
                                             String loginCompanyType) {
        getAdminAccessibleCompany(role,companyId,loginCompanyId,loginCompanyType);

        CompanyUser companyUser = companyUserRepository.waitingForApprovalUser(companyId, companyUserId);
        companyUser.updateStatusUserCompany(AffiliationStatus.REJECTED);
        companyUserRepository.save(companyUser);
        // TODO: 거부 처리 시 사용자에게 알림을 보내는 로직 추가 가능
        return MessageResponse.from("거절완료");
    }

    /**
     * 대기 중인 회사 사용자 요청을 조회합니다.
     *
     * @param role 현재 사용자 정보
     * @param companyId         대상 회사 ID
     * @return 대기 중인 사용자 목록
     */

    @Override
    public List<CompanyWaitingUserResponse> readsRequestCompanyUser(String role, Long companyId,Long loginCompanyId ,
                                                                    String loginCompanyType) {
        getAdminAccessibleCompany(role,companyId,loginCompanyId,loginCompanyType);

        List<CompanyWaitingUserResponse> companyUserResponses = new ArrayList<>();
        List<CompanyUser> companyUsers = companyUserRepository.waitingForApprovalUsers(companyId);
        for (CompanyUser companyUser : companyUsers) {
            companyUserResponses.add(CompanyWaitingUserResponse.from(companyUser));
        }
        return companyUserResponses;
    }


    private boolean isAdmin(String role) {
        return role.equals("ADMIN");
    }
    private void getAdminAccessibleCompany(String role, Long companyId, Long loginCompanyId, String loginCompanyType) {
        if(!isAdmin(role)){
            verifyAdminAffiliation(companyId, loginCompanyId,loginCompanyType);
        }
    }
    private void verifyAdminAffiliation(Long companyId,Long loginCompanyId,String loginCompanyType) {
        if(!Objects.equals(companyId, loginCompanyId) || !Objects.equals(loginCompanyType, "ADMIN")){
            throw new IllegalAccessError("권한이 없습니다.");
        }
    }
    private void getReadableCompany(String role, Long companyId,Long loginCompanyId) {
        if (!isAdmin(role)) {
            verifyUserAffiliation(companyId,loginCompanyId);
        }
    }
    private void verifyUserAffiliation(Long companyId,Long loginCompanyId) {
        if (!Objects.equals(companyId, loginCompanyId)) {
            throw new IllegalAccessError("권한이 없습니다.");
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
