package com.gogetdata.company.domain.service;

import com.gogetdata.company.application.CompanyTeamUserService;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyteam.CompanyTeamResponse;
import com.gogetdata.company.application.dto.companyteamuser.AcceptJoinRequest;
import com.gogetdata.company.application.dto.companyteamuser.CompanyTeamUserResponse;
import com.gogetdata.company.application.dto.companyteamuser.UpdateUserPerMissionRequest;
import com.gogetdata.company.domain.entity.*;
import com.gogetdata.company.domain.repository.companyteamuser.CompanyTeamUserRepository;
import com.gogetdata.company.domain.repository.companyuser.CompanyUserRepository;
import com.gogetdata.company.infrastructure.filter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CompanyTeamUserServiceImpl implements CompanyTeamUserService {
    private final CompanyTeamUserRepository companyTeamUserRepository;
    private final CompanyUserRepository companyUserRepository;
    /**
     * 팀에 가입을 신청합니다.
     *
     * @param customUserDetails        현재 사용자 정보
     * @param companyId          회사 ID
     * @param companyTeamId      팀 ID
     * @return 성공 메시지
     */
    @Override
    public MessageResponse applyToJoinTeam (CustomUserDetails customUserDetails, Long companyId, Long companyTeamId) {
        CompanyUser companyUser = companyUserRepository.isApprovalUser(companyId, customUserDetails.userId());
        if(companyUser==null){
            throw new  IllegalArgumentException("소속이아님");
        }
        CompanyTeamUser companyTeamUser = CompanyTeamUser.create(
                companyTeamId,
                companyUser.getUserId(),
                companyUser.getUserName(),
                companyUser.getEmail(),
                CompanyTeamUserStatus.PENDING,
                CompanyTeamUserType.UNASSIGN);
        companyTeamUserRepository.save(companyTeamUser);
        return MessageResponse.from("요청완료");
    }
    /**
     * 팀 가입 요청을 승인합니다.
     *
     * @param customUserDetails         현재 사용자 정보
     * @param companyId           회사 ID
     * @param companyTeamId       팀 ID
     * @param acceptJoinRequest  승인할 가입 요청 목록
     * @return 승인 결과 메시지 목록
     */
    @Override
    public List<MessageResponse> acceptJoinRequest(CustomUserDetails customUserDetails , Long companyId , Long companyTeamId , List<AcceptJoinRequest> acceptJoinRequest) {
        List<MessageResponse> message = new ArrayList<>();
        // 권한체크 admin or companyAdmin or teamAdmin
        isAdminOrAffiliatedCompany(customUserDetails,companyTeamId);

        // 요청된 팀 유저 ID 목록 추출
        List<Long> teamUserIds = acceptJoinRequest.stream()
                .map(AcceptJoinRequest::getCompanyTeamUserId)
                .toList();
        // 팀 유저 조회
        List<CompanyTeamUser> companyTeamUsers = companyTeamUserRepository.isExistUsers(companyId, companyTeamId, teamUserIds);
        // 요청을 맵으로 변환 (companyTeamUserId를 키로 사용)
        Map<Long, AcceptJoinRequest> requestMap = acceptJoinRequest.stream()
                .collect(Collectors.toMap(AcceptJoinRequest::getCompanyTeamUserId, req -> req));
        // 팀 유저 승인 처리
        for (CompanyTeamUser companyTeamUser : companyTeamUsers) {
            AcceptJoinRequest request = requestMap.get(companyTeamUser.getCompanyTeamUserId());
            if (request != null) {
                companyTeamUser.acceptUser(CompanyTeamUserType.valueOf(request.getType()));
                message.add(new MessageResponse(companyTeamUser.getUserName() + "등록"));
            }
        }
        // 변경된 팀 유저 저장
        companyTeamUserRepository.saveAll(companyTeamUsers);
        return message;
    }
    /**
     * 팀 가입 요청을 거절합니다.
     *
     * @param customUserDetails         현재 사용자 정보
     * @param companyId           회사 ID
     * @param companyTeamId       팀 ID
     * @param userId   팀 사용자 ID
     * @return 거절 결과 메시지
     */
    @Override
    public MessageResponse rejectJoinRequest(CustomUserDetails customUserDetails , Long companyId , Long companyTeamId,Long userId) {
        isAdminOrAffiliatedCompany(customUserDetails,companyTeamId);

        CompanyTeamUser companyTeamUser = companyTeamUserRepository.isExistUser(companyId,companyTeamId,userId);
        companyTeamUser.rejectUser();
        companyTeamUserRepository.save(companyTeamUser);
        return MessageResponse.from("거절");
    }
    /**
     * 팀 사용자의 권한을 업데이트합니다.
     *
     * @param customUserDetails                현재 사용자 정보
     * @param companyId                  회사 ID
     * @param companyTeamId              팀 ID
     * @param userId                     사용자 ID
     * @param updateUserPerMissionRequest 권한 업데이트 요청 DTO
     * @return 권한 업데이트 결과 메시지
     */
    @Override
    public MessageResponse updateUserPermission(CustomUserDetails customUserDetails , Long companyId , Long companyTeamId, Long userId,
                                                UpdateUserPerMissionRequest updateUserPerMissionRequest) {
        isAdminOrAffiliatedCompany(customUserDetails,companyTeamId);
        CompanyTeamUser companyTeamUser = companyTeamUserRepository.isExistUser(companyId,companyTeamId,userId);
        companyTeamUser.updateUserType(CompanyTeamUserType.valueOf(updateUserPerMissionRequest.getType()));
        companyTeamUserRepository.save(companyTeamUser);
        return MessageResponse.from("변경");
    }
    /**
     * 사용자가 속한 팀 목록을 조회합니다.
     *
     * @param customUserDetails 현재 사용자 정보
     * @return 팀 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<CompanyTeamResponse> getMyTeams(CustomUserDetails customUserDetails) {
        List<CompanyTeam> companyTeams = companyTeamUserRepository.getMyTeams(customUserDetails.userId());
        return companyTeams.stream()
                .map(CompanyTeamResponse::from)
                .collect(Collectors.toList());
    }
    /**
     * 팀 내 사용자 목록을 조회합니다.
     *
     * @param customUserDetails   현재 사용자 정보
     * @param companyTeamId 팀 ID
     * @return 팀 사용자 목록
     */
    @Override
    public List<CompanyTeamUserResponse> getUsersInTeam(CustomUserDetails customUserDetails , Long companyTeamId) {
        if(!isAdmin(customUserDetails.getAuthorities().toString())){
            boolean isCompanyAdminOrTeam =  companyTeamUserRepository.isExistAdminOrUser(companyTeamId,customUserDetails.userId());
            if(!isCompanyAdminOrTeam){
                throw new IllegalArgumentException("팀소속아님");
            }
        }
        List<CompanyTeamUserResponse> companyTeamUserResponses = new ArrayList<>();
        List<CompanyTeamUser> companyTeamUsers = companyTeamUserRepository.isExistUsers(companyTeamId);
        for (CompanyTeamUser companyTeamUser : companyTeamUsers) {
            companyTeamUserResponses.add(CompanyTeamUserResponse.from(companyTeamUser));
        }
        return companyTeamUserResponses;
    }
    /**
     * 팀에서 사용자를 삭제합니다.
     *
     * @param customUserDetails         현재 사용자 정보
     * @param companyId           회사 ID
     * @param companyTeamId       팀 ID
     * @param companyTeamUserId   팀 사용자 ID
     * @return 삭제 결과 메시지
     */
    @Override
    public MessageResponse deleteUserFromTeam(CustomUserDetails customUserDetails , Long companyId , Long companyTeamId,Long companyTeamUserId) {
        boolean isSelfDeletion = customUserDetails.userId().equals(companyTeamUserId);
        if (!isSelfDeletion) {
            isAdminOrAffiliatedCompany(customUserDetails, companyTeamId);
        }
        CompanyTeamUser companyTeamUser = validateCompanyTeamUserNotDeleted(isExistCompanyTeamUser(companyTeamUserId));
        companyTeamUser.deleteUser();
        companyTeamUserRepository.save(companyTeamUser);

        return MessageResponse.from("삭제");
    }

    @Override
    public String getUserInTeam(Long companyTeamId, Long userId) {
        CompanyTeamUser companyTeamUser=companyTeamUserRepository.checkUserInTeam(companyTeamId,userId);
        return companyTeamUser == null ? "null" : String.valueOf(companyTeamUser.getCompanyTeamUserType());
    }

    @Override
    public List<CompanyTeamUserResponse> searchTeamUser(CustomUserDetails customUserDetails, Long companyId, Long companyTeamId, String userName) {
        if(!isAdmin(customUserDetails.getAuthorities().toString())){
            boolean isCompanyAdminOrTeam =  companyTeamUserRepository.isExistAdminOrUser(companyTeamId,customUserDetails.userId());
            if(!isCompanyAdminOrTeam){
                throw new IllegalArgumentException("팀소속아님");
            }
        }
        List<CompanyTeamUserResponse> userResponses = new ArrayList<>();
        List<CompanyTeamUser> users = companyTeamUserRepository.getSearchTeamUser(companyTeamId,userName);
        for (CompanyTeamUser user : users) {
            userResponses.add(CompanyTeamUserResponse.from(user));
        }
        return userResponses;
    }

    public void isAdminOrAffiliatedCompany(CustomUserDetails customUserDetails,Long companyTeamId){
        if (isAdmin(customUserDetails.getAuthorities().toString())) {
            return;
        }
        verifyAdminAffiliation(customUserDetails.userId(), companyTeamId);
    }
    private boolean isAdmin(String role){
        return role.equals("ADMIN");
    }

    private void verifyAdminAffiliation(Long loginUserId,Long companyTeamId){
        boolean isAdminOrTeamAdmin =  companyTeamUserRepository.isExistAdminUser(companyTeamId,loginUserId);
        if (!isAdminOrTeamAdmin) {
            throw new IllegalArgumentException("권한없음");
        }
    }
    public CompanyTeamUser isExistCompanyTeamUser(Long companyTeamUserId) {
        return companyTeamUserRepository.findById(companyTeamUserId)
                .orElseThrow(() -> new NoSuchElementException("No user found with id " + companyTeamUserId));
    }
    public CompanyTeamUser validateCompanyTeamUserNotDeleted(CompanyTeamUser companyTeamUser) {
        if (companyTeamUser.isDeleted()) {
            throw new NoSuchElementException("User with id " + companyTeamUser.getUserId() + " is deleted.");
        }
        return companyTeamUser;
    }
}
