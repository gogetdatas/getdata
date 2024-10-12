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
import com.gogetdata.company.infrastructure.filter.CustomUserDetails;
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
    public MessageResponse requestCompanyTeam(CustomUserDetails customUserDetails, Long companyId, RequestCompanyTeamRequest requestCompanyTeamRequest) {
        validateUserAffiliation(customUserDetails,companyId);

        CompanyTeam companyTeam = CompanyTeam.create(companyId, requestCompanyTeamRequest.getCompanyTeamName(), CompanyTeamStatus.PENDING);
        companyTeam.setCreatedBy(customUserDetails.userId());
        companyTeamRepository.save(companyTeam);
        return MessageResponse.from("요청완료");
    }

    @Override
    public List<RequestCompanyTeamResponse> requestReadCompanyTeam(CustomUserDetails customUserDetails, Long companyId) {
        authorizeAdminOrCompanyAdmin(customUserDetails,companyId);

        List<CompanyTeam> companyTeams = companyTeamRepository.readRequestCompanyTeams(companyId);
        return companyTeams.stream()
                .map(RequestCompanyTeamResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageResponse approveRequestCompanyTeam(CustomUserDetails customUserDetails, Long companyTeamId,Long companyId) {
        authorizeAdminOrCompanyAdmin(customUserDetails,companyId);
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
    public MessageResponse updateCompanyTeamName(CustomUserDetails customUserDetails, Long companyTeamId, Long companyId, UpdateTeamRequest updateTeamRequest) {
        validateUserAffiliation(customUserDetails,companyId);

        CompanyTeam companyTeam=validateCompanyTeamNotDeleted(isExistCompanyTeam(companyTeamId));
        companyTeam.updateTeamName(updateTeamRequest.getTeamName());
        companyTeamRepository.save(companyTeam);
        return MessageResponse.from("이름변경 변경이름 :" + updateTeamRequest.getTeamName());
    }

    @Override
    @Transactional
    public MessageResponse deleteCompanyTeam(CustomUserDetails customUserDetails, Long companyTeamId, Long companyId) {
        validateUserAffiliation(customUserDetails,companyId);

        CompanyTeam companyTeam=validateCompanyTeamNotDeleted(isExistCompanyTeam(companyTeamId));
        companyTeam.deleteTeam();
        companyTeamRepository.save(companyTeam);
        return MessageResponse.from("삭제완료");
    }

    @Override
    @Transactional
    public MessageResponse rejectRequestCompanyTeam(CustomUserDetails customUserDetails, Long companyTeamId, Long companyId) {
        validateUserAffiliation(customUserDetails,companyId);

        CompanyTeam companyTeam = companyTeamRepository.readRequestCompanyTeam(companyTeamId);
        companyTeam.rejectTeam();
        companyTeamRepository.save(companyTeam);
        return MessageResponse.from("거절");
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
    private void validateUserAffiliation(CustomUserDetails customUserDetails , Long companyId) {
        if (isAdmin(customUserDetails.getAuthorities().toString())) {
            return;
        }
        CompanyUser loginUser = companyUserRepository.isApprovalUser(companyId, customUserDetails.userId());
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
