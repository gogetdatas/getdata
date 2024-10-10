package com.gogetdata.company.presentation;

import com.gogetdata.company.application.CompanyTeamService;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyteam.RequestCompanyTeamRequest;
import com.gogetdata.company.application.dto.companyteam.RequestCompanyTeamResponse;
import com.gogetdata.company.application.dto.companyteam.UpdateTeamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyTeamController {
    private final CompanyTeamService companyTeamService;
    @PostMapping("/{companyId}/teams")
    public ResponseEntity<MessageResponse> requestCompanyTeam(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                              @RequestHeader(value = "X-Role") String role,
                                                              @RequestHeader(value = "X-Company-Id") Long loginCompanyId,
                                                              @RequestBody RequestCompanyTeamRequest requestCompanyTeamRequest,
                                                               @PathVariable Long companyId ) {
        MessageResponse response = companyTeamService.requestCompanyTeam(loginUserId,role,companyId,requestCompanyTeamRequest,loginCompanyId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{companyId}/teams/requests")
    public ResponseEntity<List<RequestCompanyTeamResponse>> requestReadCompanyTeam(@RequestHeader(value = "X-Role") String role,
                                                                                   @RequestHeader(value = "X-Company-Id") Long loginCompanyId,
                                                                                   @RequestHeader(value = "X-Company-Role") String loginCompanyRole,
                                                                                   @PathVariable Long companyId ) {
        List<RequestCompanyTeamResponse> response = companyTeamService.requestReadCompanyTeam(role,companyId,loginCompanyId,loginCompanyRole);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{companyId}/teams/{companyTeamId}/approve")
    public ResponseEntity<MessageResponse> approveRequestCompanyTeam(@RequestHeader(value = "X-Role") String role,
                                                                     @RequestHeader(value = "X-Company-Id") Long loginCompanyId,
                                                                     @RequestHeader(value = "X-Company-Role") String loginCompanyRole,
                                                                     @PathVariable Long companyId ,
                                                                     @PathVariable Long companyTeamId) {
        MessageResponse response = companyTeamService.approveRequestCompanyTeam(role,companyTeamId,companyId,loginCompanyId,loginCompanyRole);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{companyId}/teams/{companyTeamId}/name")
    public ResponseEntity<MessageResponse> updateCompanyTeamName(@RequestHeader(value = "X-Role") String role,
                                                                 @RequestHeader(value = "X-Company-Id") Long loginCompanyId,
                                                                 @RequestHeader(value = "X-Company-Role") String loginCompanyRole,
                                                                 @PathVariable Long companyId ,
                                                                 @PathVariable Long companyTeamId,
                                                                 @RequestBody UpdateTeamRequest updateTeamRequest) {
        MessageResponse response = companyTeamService.updateCompanyTeamName(role,companyTeamId,companyId,updateTeamRequest,loginCompanyId,loginCompanyRole);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{companyId}/teams/{companyTeamId}")
    public ResponseEntity<MessageResponse> deleteCompanyTeam(@RequestHeader(value = "X-Role") String role,
                                                             @RequestHeader(value = "X-Company-Id") Long loginCompanyId,
                                                             @RequestHeader(value = "X-Company-Role") String loginCompanyRole,
                                                             @PathVariable Long companyId ,
                                                             @PathVariable Long companyTeamId) {
        MessageResponse response = companyTeamService.deleteCompanyTeam(role,companyTeamId,companyId,loginCompanyId,loginCompanyRole);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{companyId}/teams/{companyTeamId}/reject")
    public ResponseEntity<MessageResponse> rejectRequestCompanyTeam(@RequestHeader(value = "X-Role") String role,
                                                                    @RequestHeader(value = "X-Company-Id") Long loginCompanyId,
                                                                    @RequestHeader(value = "X-Company-Role") String loginCompanyRole,
                                                                    @PathVariable Long companyId ,
                                                                    @PathVariable Long companyTeamId) {
        MessageResponse response = companyTeamService.rejectRequestCompanyTeam(role,companyTeamId,companyId,loginCompanyId,loginCompanyRole);
        return ResponseEntity.ok(response);
    }
}
