package com.gogetdata.company.presentation;

import com.gogetdata.company.application.CompanyTeamService;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyteam.RequestCompanyTeamRequest;
import com.gogetdata.company.application.dto.companyteam.RequestCompanyTeamResponse;
import com.gogetdata.company.application.dto.companyteam.UpdateTeamRequest;
import com.gogetdata.company.infrastructure.filter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyTeamController {
    private final CompanyTeamService companyTeamService;
    @PostMapping("/{companyId}/teams")
    public ResponseEntity<MessageResponse> requestCompanyTeam(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                               @RequestBody RequestCompanyTeamRequest requestCompanyTeamRequest,
                                                               @PathVariable Long companyId ) {
        MessageResponse response = companyTeamService.requestCompanyTeam(customUserDetails,companyId,requestCompanyTeamRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{companyId}/teams/requests")
    public ResponseEntity<List<RequestCompanyTeamResponse>> requestReadCompanyTeam(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                                   @PathVariable Long companyId ) {
        List<RequestCompanyTeamResponse> response = companyTeamService.requestReadCompanyTeam(customUserDetails,companyId);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{companyId}/teams/{companyTeamId}/approve")
    public ResponseEntity<MessageResponse> approveRequestCompanyTeam(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                                      @PathVariable Long companyId ,
                                                                                      @PathVariable Long companyTeamId) {
        MessageResponse response = companyTeamService.approveRequestCompanyTeam(customUserDetails,companyTeamId,companyId);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{companyId}/teams/{companyTeamId}/name")
    public ResponseEntity<MessageResponse> updateCompanyTeamName(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                 @PathVariable Long companyId ,
                                                                 @PathVariable Long companyTeamId,
                                                                 @RequestBody UpdateTeamRequest updateTeamRequest) {
        MessageResponse response = companyTeamService.updateCompanyTeamName(customUserDetails,companyTeamId,companyId,updateTeamRequest);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{companyId}/teams/{companyTeamId}")
    public ResponseEntity<MessageResponse> deleteCompanyTeam(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                 @PathVariable Long companyId ,
                                                                 @PathVariable Long companyTeamId) {
        MessageResponse response = companyTeamService.deleteCompanyTeam(customUserDetails,companyTeamId,companyId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{companyId}/teams/{companyTeamId}/reject")
    public ResponseEntity<MessageResponse> rejectRequestCompanyTeam(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                             @PathVariable Long companyId ,
                                                             @PathVariable Long companyTeamId) {
        MessageResponse response = companyTeamService.rejectRequestCompanyTeam(customUserDetails,companyTeamId,companyId);
        return ResponseEntity.ok(response);
    }
}
