package com.gogetdata.company.presentation;

import com.gogetdata.company.application.CompanyTeamUserService;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyteam.CompanyTeamResponse;
import com.gogetdata.company.application.dto.companyteamuser.AcceptJoinRequest;
import com.gogetdata.company.application.dto.companyteamuser.CompanyTeamUserResponse;
import com.gogetdata.company.application.dto.companyteamuser.UpdateUserPerMissionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyTeamUserController {
    private final CompanyTeamUserService companyTeamUserService;
    @PostMapping("/{companyId}/teams/{companyTeamId}/users")
    public ResponseEntity<MessageResponse> applyToJoinTeam(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                           @PathVariable Long companyId,
                                                           @PathVariable Long companyTeamId ) {
        MessageResponse response = companyTeamUserService.applyToJoinTeam(loginUserId,companyId,companyTeamId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{companyId}/teams/{companyTeamId}/users")
    public ResponseEntity<List<MessageResponse>> acceptJoinRequest(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                                   @RequestHeader(value = "X-Role") String role,
                                                                   @RequestHeader(value = "X-Company-Id") Long loginCompanyId,
                                                                   @RequestHeader(value = "X-Company-Role") String loginCompanyRole,
                                                                   @PathVariable Long companyId,
                                                                   @PathVariable Long companyTeamId ,
                                                                   @RequestBody List<AcceptJoinRequest> acceptJoinRequest) {
        List<MessageResponse> response = companyTeamUserService.acceptJoinRequest(loginUserId,role,companyId,companyTeamId,acceptJoinRequest,loginCompanyId,loginCompanyRole);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{companyId}/teams/{companyTeamId}/users/{teamUserId}/reject")
    public ResponseEntity<MessageResponse> rejectJoinRequest(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                             @RequestHeader(value = "X-Role") String role,
                                                             @RequestHeader(value = "X-Company-Id") Long loginCompanyId,
                                                             @RequestHeader(value = "X-Company-Role") String loginCompanyRole,
                                                             @PathVariable Long companyId,
                                                             @PathVariable Long companyTeamId ,
                                                             @PathVariable Long teamUserId) {
        MessageResponse response = companyTeamUserService.rejectJoinRequest(loginUserId,role,companyId,companyTeamId,teamUserId,loginCompanyId,loginCompanyRole);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{companyId}/teams/{companyTeamId}/users/{teamUserId}/permission")
    public ResponseEntity<MessageResponse> updateUserPermission(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                                @RequestHeader(value = "X-Role") String role,
                                                                @RequestHeader(value = "X-Company-Id") Long loginCompanyId,
                                                                @RequestHeader(value = "X-Company-Role") String loginCompanyRole,
                                                                @PathVariable Long companyId,
                                                                @PathVariable Long companyTeamId ,
                                                                @PathVariable Long teamUserId,
                                                                @RequestBody UpdateUserPerMissionRequest updateUserPerMissionRequest) {
        MessageResponse response = companyTeamUserService.updateUserPermission(loginUserId,role,companyId,companyTeamId,teamUserId,updateUserPerMissionRequest,loginCompanyId,loginCompanyRole);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/me/teams")
    public ResponseEntity<List<CompanyTeamResponse>> getMyTeams(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                                @RequestHeader(value = "X-Role") String role) {
        List<CompanyTeamResponse> response = companyTeamUserService.getMyTeams(loginUserId,role);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/teams/{companyTeamId}/users")
    public ResponseEntity<List<CompanyTeamUserResponse>> getUsersInTeam(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                                        @RequestHeader(value = "X-Role") String role,
                                                                        @PathVariable Long companyTeamId) {
        List<CompanyTeamUserResponse> response = companyTeamUserService.getUsersInTeam(loginUserId,role,companyTeamId);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{companyId}/teams/{companyTeamId}/users/{teamUserId}")
    public ResponseEntity<MessageResponse> deleteUserFromTeam(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                              @RequestHeader(value = "X-Role") String role,
                                                              @RequestHeader(value = "X-Company-Id") Long loginCompanyId,
                                                              @RequestHeader(value = "X-Company-Role") String loginCompanyRole,

                                                              @PathVariable Long companyId,
                                                              @PathVariable Long companyTeamId ,
                                                              @PathVariable Long teamUserId) {
        MessageResponse response = companyTeamUserService.deleteUserFromTeam(loginUserId,role,companyId,companyTeamId,teamUserId,loginCompanyId,loginCompanyRole);
        return ResponseEntity.ok(response);
    }
}
