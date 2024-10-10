package com.gogetdata.company.domain.service;

import com.gogetdata.company.application.CompanyTeamService;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyteam.RequestCompanyTeamRequest;
import com.gogetdata.company.application.dto.companyteam.RequestCompanyTeamResponse;
import com.gogetdata.company.application.dto.companyteam.UpdateTeamRequest;
import com.gogetdata.company.domain.entity.*;
import com.gogetdata.company.domain.repository.companyteam.CompanyTeamRepository;
import com.gogetdata.company.domain.repository.companyteamuser.CompanyTeamUserRepository;
import com.gogetdata.company.domain.repository.companyuser.CompanyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyTeamServiceImpl implements CompanyTeamService {
    private final CompanyUserRepository companyUserRepository;
    private final CompanyTeamRepository companyTeamRepository;
    private final CompanyTeamUserRepository companyTeamUserRepository;
    @Override
    public MessageResponse requestCompanyTeam(Long loginUserId,String role, Long companyId, RequestCompanyTeamRequest requestCompanyTeamRequest,Long loginCompanyId) {
        getReadableCompany(role,companyId,loginCompanyId);
        CompanyTeam companyTeam = CompanyTeam.create(companyId, requestCompanyTeamRequest.getCompanyTeamName(), CompanyTeamStatus.PENDING);
        companyTeam.setCreatedBy(loginUserId);
        companyTeamRepository.save(companyTeam);
        return MessageResponse.from("요청완료");
    }

    @Override
    public List<RequestCompanyTeamResponse> requestReadCompanyTeam(String role, Long companyId,Long loginCompanyId,String loginUserType) {
        getAdminAccessibleCompany(role,companyId,loginCompanyId,loginUserType);

        List<CompanyTeam> companyTeams = companyTeamRepository.readRequestCompanyTeams(companyId);
        return companyTeams.stream()
                .map(RequestCompanyTeamResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageResponse approveRequestCompanyTeam(String role, Long companyTeamId,Long companyId,Long loginCompanyId,String loginUserType) {
        getAdminAccessibleCompany(role,companyId,loginCompanyId,loginUserType);
        CompanyTeam companyTeam = companyTeamRepository.readRequestCompanyTeam(companyTeamId);
        companyTeam.approveTeam();
        companyTeamRepository.save(companyTeam);

        CompanyUser companyUser = companyUserRepository.isApprovalUser(companyId,companyTeam.getCreatedBy());
        CompanyTeamUser companyTeamUser = CompanyTeamUser.create(companyTeamId,
                companyUser.getUserId(),
                companyUser.getUserName(),
                companyUser.getEmail(),
                CompanyTeamUserStatus.APPROVED,
                CompanyTeamUserType.ADMIN);
        companyTeamUserRepository.save(companyTeamUser);
        return MessageResponse.from("승인");
    }

    @Override
    @Transactional
    public MessageResponse updateCompanyTeamName(String role, Long companyTeamId, Long companyId, UpdateTeamRequest updateTeamRequest,Long loginCompanyId,String loginUserType) {
        getAdminAccessibleCompany(role,companyId,loginCompanyId,loginUserType);

        CompanyTeam companyTeam=validateCompanyTeamNotDeleted(findCompanyTeam(companyTeamId));
        companyTeam.updateTeamName(updateTeamRequest.getTeamName());
        companyTeamRepository.save(companyTeam);
        return MessageResponse.from("이름변경 변경이름 :" + updateTeamRequest.getTeamName());
    }

    @Override
    @Transactional
    public MessageResponse deleteCompanyTeam(String role, Long companyTeamId, Long companyId,Long loginCompanyId,String loginUserType) {
        getAdminAccessibleCompany(role,companyId,loginCompanyId,loginUserType);

        CompanyTeam companyTeam=validateCompanyTeamNotDeleted(findCompanyTeam(companyTeamId));
        companyTeam.deleteTeam();
        companyTeamRepository.save(companyTeam);
        return MessageResponse.from("삭제완료");
    }

    @Override
    @Transactional
    public MessageResponse rejectRequestCompanyTeam(String role, Long companyTeamId, Long companyId,Long loginCompanyId,String loginUserType) {
        getAdminAccessibleCompany(role,companyId,loginCompanyId,loginUserType);
        CompanyTeam companyTeam = companyTeamRepository.readRequestCompanyTeam(companyTeamId);
        companyTeam.rejectTeam();
        companyTeamRepository.save(companyTeam);
        return MessageResponse.from("거절");
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
    private CompanyTeam findCompanyTeam(Long companyTeamId) {
        return companyTeamRepository.findById(companyTeamId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID로 팀을 찾을 수 없습니다: " + companyTeamId));
    }

    public CompanyTeam validateCompanyTeamNotDeleted(CompanyTeam companyTeam) {
        if (companyTeam.isDeleted()) {
            throw new NoSuchElementException("User with id " + companyTeam.getCompanyTeamId() + " is deleted.");
        }
        return companyTeam;
    }
}
