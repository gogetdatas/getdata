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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CompanyTeamUserServiceImpl implements CompanyTeamUserService {
    private final CompanyTeamUserRepository companyTeamUserRepository;
    private final CompanyUserRepository companyUserRepository;
    /**
     * 팀에 가입을 신청합니다.
     *
     * @param loginUserId,role        현재 사용자 정보
     * @param companyId          회사 ID
     * @param companyTeamId      팀 ID
     * @return 성공 메시지
     */
    @Override
    public MessageResponse applyToJoinTeam (Long loginUserId, Long companyId,Long companyTeamId) {
        CompanyUser companyUser = companyUserRepository.isApprovalUser(companyId, loginUserId);
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
     * @param loginUserId,role         현재 사용자 정보
     * @param companyId           회사 ID
     * @param companyTeamId       팀 ID
     * @param acceptJoinRequest  승인할 가입 요청 목록
     * @return 승인 결과 메시지 목록
     */
    @Override
    public List<MessageResponse> acceptJoinRequest(Long loginUserId,String role , Long companyId , Long companyTeamId , List<AcceptJoinRequest> acceptJoinRequest , Long loginCompanyId,String loginCompanyType) {
        List<MessageResponse> message = new ArrayList<>();
        // 권한체크 admin or companyAdmin or teamAdmin
        getAdminAccessibleCompany(loginUserId,role,companyTeamId,loginCompanyId,loginCompanyType,companyId);

        // 요청된 팀 유저 ID 목록 추출
        List<Long> teamUserIds = acceptJoinRequest.stream()
                .map(AcceptJoinRequest::getCompanyTeamUserId)
                .toList();
        // 팀 유저 조회
        List<CompanyTeamUser> companyTeamUsers = companyTeamUserRepository.isExistUsers(companyTeamId, teamUserIds);
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
     * @param loginUserId,role         현재 사용자 정보
     * @param companyId           회사 ID
     * @param companyTeamId       팀 ID
     * @param userId   팀 사용자 ID
     * @return 거절 결과 메시지
     */
    @Override
    public MessageResponse rejectJoinRequest(Long loginUserId,String role , Long companyId , Long companyTeamId,Long userId,Long loginCompanyId,String loginCompanyType) {
        getAdminAccessibleCompany(loginUserId,role,companyTeamId,loginCompanyId,loginCompanyType,companyId);

        CompanyTeamUser companyTeamUser = companyTeamUserRepository.isExistUser(companyTeamId,userId);
        companyTeamUser.rejectUser();
        companyTeamUserRepository.save(companyTeamUser);
        return MessageResponse.from("거절");
    }
    /**
     * 팀 사용자의 권한을 업데이트합니다.
     *
     * @param loginUserId,role                현재 사용자 정보
     * @param companyId                  회사 ID
     * @param companyTeamId              팀 ID
     * @param updateUserPerMissionRequest 권한 업데이트 요청 DTO
     * @return 권한 업데이트 결과 메시지
     */
    @Override
    public MessageResponse updateUserPermission(Long loginUserId,String role , Long companyId , Long companyTeamId,Long companyTeamUserId,
                                                UpdateUserPerMissionRequest updateUserPerMissionRequest,Long loginCompanyId,String loginCompanyType) {
        getAdminAccessibleCompany(loginUserId,role,companyTeamId,loginCompanyId,loginCompanyType,companyId);
        CompanyTeamUser companyTeamUser = companyTeamUserRepository.isApproveUser(companyTeamId,companyTeamUserId);
        companyTeamUser.updateUserType(CompanyTeamUserType.valueOf(updateUserPerMissionRequest.getType()));
        companyTeamUserRepository.save(companyTeamUser);
        return MessageResponse.from("변경");
    }
    /**
     * 사용자가 속한 팀 목록을 조회합니다.
     *
     * @param loginUserId,role 현재 사용자 정보
     * @return 팀 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<CompanyTeamResponse> getMyTeams(Long loginUserId,String role) {
        List<CompanyTeam> companyTeams = companyTeamUserRepository.getMyTeams(loginUserId);
        return companyTeams.stream()
                .map(CompanyTeamResponse::from)
                .collect(Collectors.toList());
    }
    /**
     * 팀 내 사용자 목록을 조회합니다.
     *
     * @param loginUserId,role   현재 사용자 정보
     * @param companyTeamId 팀 ID
     * @return 팀 사용자 목록
     */
    @Override
    public List<CompanyTeamUserResponse> getUsersInTeam(Long loginUserId,String role , Long companyTeamId) {
        if(!isAdmin(role)){
            boolean isExistUserInTeam =  companyTeamUserRepository.isExistUserInTeam(companyTeamId,loginUserId);
            if(!isExistUserInTeam){
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
     * @param loginUserId,role         현재 사용자 정보
     * @param companyId           회사 ID
     * @param companyTeamId       팀 ID
     * @param companyTeamUserId   팀 사용자 ID
     * @return 삭제 결과 메시지
     */
    @Override
    public MessageResponse deleteUserFromTeam(Long loginUserId,String role , Long companyId , Long companyTeamId,Long companyTeamUserId,Long loginCompanyId,String loginCompanyType) {
        CompanyTeamUser companyTeamUser = validateCompanyTeamUserNotDeleted(isExistCompanyTeamUser(companyTeamUserId));
        boolean isSelfDeletion = loginUserId.equals(companyTeamUser.getCompanyTeamId());
        if (!isSelfDeletion) {
            getAdminAccessibleCompany(loginUserId,role,companyTeamId,loginCompanyId,loginCompanyType,companyId);
        }

        companyTeamUser.deleteUser();
        companyTeamUserRepository.save(companyTeamUser);

        return MessageResponse.from("삭제");
    }

    public void getAdminAccessibleCompany(Long loginUserId,String role,Long companyTeamId,Long loginCompanyId , String loginCompanyType,Long companyId){
        if (isAdmin(role)) {
            return;
        }
        verifyAdminAffiliation(loginUserId, companyTeamId,loginCompanyId,loginCompanyType,companyId);
    }
    private boolean isAdmin(String role){
        return role.equals("ADMIN");
    }
    private void verifyAdminAffiliation(Long loginUserId,Long companyTeamId,Long loginCompanyId , String loginCompanyType,Long companyId) {
        if(!Objects.equals(companyId, loginCompanyId)){
            throw new IllegalAccessError("권한이 없습니다.");
        }
        if(!Objects.equals(loginCompanyType, "ADMIN")){
            if(!companyTeamUserRepository.isExistAdminUser(loginUserId,companyTeamId)){
                throw new IllegalAccessError("권한이 없습니다.");
            }
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
