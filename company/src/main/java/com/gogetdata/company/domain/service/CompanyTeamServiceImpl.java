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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyTeamServiceImpl implements CompanyTeamService {
    private final CompanyUserRepository companyUserRepository;
    private final CompanyTeamRepository companyTeamRepository;
    private final CompanyTeamUserRepository companyTeamUserRepository;
    @Override
    public MessageResponse requestCompanyTeam(Long loginUserId,String role, Long companyId, RequestCompanyTeamRequest requestCompanyTeamRequest) {
        validateUserAffiliation(loginUserId,role,companyId);

        CompanyTeam companyTeam = CompanyTeam.create(companyId, requestCompanyTeamRequest.getCompanyTeamName(), CompanyTeamStatus.PENDING);
        companyTeam.setCreatedBy(loginUserId);
        companyTeamRepository.save(companyTeam);
        return MessageResponse.from("요청완료");
    }

    @Override
    public List<RequestCompanyTeamResponse> requestReadCompanyTeam(Long loginUserId,String role, Long companyId) {
        authorizeAdminOrCompanyAdmin(loginUserId,role,companyId);

        List<CompanyTeam> companyTeams = companyTeamRepository.readRequestCompanyTeams(companyId);
        return companyTeams.stream()
                .map(RequestCompanyTeamResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageResponse approveRequestCompanyTeam(Long loginUserId,String role, Long companyTeamId,Long companyId) {
        authorizeAdminOrCompanyAdmin(loginUserId,role,companyId);
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
    public MessageResponse updateCompanyTeamName(Long loginUserId,String role, Long companyTeamId, Long companyId, UpdateTeamRequest updateTeamRequest) {
        validateUserAffiliation(loginUserId,role,companyId);

        CompanyTeam companyTeam=validateCompanyTeamNotDeleted(isExistCompanyTeam(companyTeamId));
        companyTeam.updateTeamName(updateTeamRequest.getTeamName());
        companyTeamRepository.save(companyTeam);
        return MessageResponse.from("이름변경 변경이름 :" + updateTeamRequest.getTeamName());
    }

    @Override
    @Transactional
    public MessageResponse deleteCompanyTeam(Long loginUserId,String role, Long companyTeamId, Long companyId) {
        validateUserAffiliation(loginUserId,role,companyId);

        CompanyTeam companyTeam=validateCompanyTeamNotDeleted(isExistCompanyTeam(companyTeamId));
        companyTeam.deleteTeam();
        companyTeamRepository.save(companyTeam);
        return MessageResponse.from("삭제완료");
    }

    @Override
    @Transactional
    public MessageResponse rejectRequestCompanyTeam(Long loginUserId,String role, Long companyTeamId, Long companyId) {
        validateUserAffiliation(loginUserId,role,companyId);

        CompanyTeam companyTeam = companyTeamRepository.readRequestCompanyTeam(companyTeamId);
        companyTeam.rejectTeam();
        companyTeamRepository.save(companyTeam);
        return MessageResponse.from("거절");
    }

    private void authorizeAdminOrCompanyAdmin(Long loginUserId,String role, Long companyId) {
        if (isAdmin(role)) {
            return;
        }

        CompanyUser loginUser = companyUserRepository.isApprovalUser(companyId, loginUserId);
        if (loginUser == null || !loginUser.getType().getAuthority().equals("ADMIN")) {
            throw new IllegalArgumentException("권한없음");
        }
    }
    private void validateUserAffiliation(Long loginUserId,String role , Long companyId) {
        if (isAdmin(role)) {
            return;
        }
        CompanyUser loginUser = companyUserRepository.isApprovalUser(companyId, loginUserId);
        if (loginUser == null) {
            throw new IllegalArgumentException("권한없음");
        }
    }
    private boolean isAdmin(String role){
        return role.equals("ADMIN");
    }
    public CompanyTeam isExistCompanyTeam(Long companyTeamId) {
        return companyTeamRepository.findById(companyTeamId)
                .orElseThrow(() -> new NoSuchElementException("No user found with id " + companyTeamId));
    }
    public CompanyTeam validateCompanyTeamNotDeleted(CompanyTeam companyTeam) {
        if (companyTeam.isDeleted()) {
            throw new NoSuchElementException("User with id " + companyTeam.getCompanyTeamId() + " is deleted.");
        }
        return companyTeam;
    }
}
