package com.gogetdata.company.presentation;

import com.gogetdata.company.application.CompanyTeamUserService;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyteam.CompanyTeamResponse;
import com.gogetdata.company.application.dto.companyteamuser.AcceptJoinRequest;
import com.gogetdata.company.application.dto.companyteamuser.CompanyTeamUserResponse;
import com.gogetdata.company.application.dto.companyteamuser.UpdateUserPerMissionRequest;
import com.gogetdata.company.infrastructure.filter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyTeamUserController {
    private final CompanyTeamUserService companyTeamUserService;
    @PostMapping("/{companyId}/teams/{companyTeamId}/users")
    public ResponseEntity<MessageResponse> applyToJoinTeam(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                           @PathVariable Long companyId,
                                                           @PathVariable Long companyTeamId ) {
        MessageResponse response = companyTeamUserService.applyToJoinTeam(customUserDetails,companyId,companyTeamId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{companyId}/teams/{companyTeamId}/users")
    public ResponseEntity<List<MessageResponse>> acceptJoinRequest(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                   @PathVariable Long companyId,
                                                                   @PathVariable Long companyTeamId ,
                                                                   @RequestBody List<AcceptJoinRequest> acceptJoinRequest) {
        List<MessageResponse> response = companyTeamUserService.acceptJoinRequest(customUserDetails,companyId,companyTeamId,acceptJoinRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{companyId}/teams/{companyTeamId}/users/{teamUserId}/reject")
    public ResponseEntity<MessageResponse> rejectJoinRequest(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                             @PathVariable Long companyId,
                                                             @PathVariable Long companyTeamId ,
                                                             @PathVariable Long teamUserId) {
        MessageResponse response = companyTeamUserService.rejectJoinRequest(customUserDetails,companyId,companyTeamId,teamUserId);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{companyId}/teams/{companyTeamId}/users/{teamUserId}/permission")
    public ResponseEntity<MessageResponse> updateUserPermission(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                @PathVariable Long companyId,
                                                                @PathVariable Long companyTeamId ,
                                                                @PathVariable Long teamUserId,
                                                                @RequestBody UpdateUserPerMissionRequest updateUserPerMissionRequest) {
        MessageResponse response = companyTeamUserService.updateUserPermission(customUserDetails,companyId,companyTeamId,teamUserId,updateUserPerMissionRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/me/teams")
    public ResponseEntity<List<CompanyTeamResponse>> getMyTeams(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<CompanyTeamResponse> response = companyTeamUserService.getMyTeams(customUserDetails);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/teams/{companyTeamId}/users")
    public ResponseEntity<List<CompanyTeamUserResponse>> getUsersInTeam(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                        @PathVariable Long companyTeamId) {
        List<CompanyTeamUserResponse> response = companyTeamUserService.getUsersInTeam(customUserDetails,companyTeamId);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{companyId}/teams/{companyTeamId}/users/{teamUserId}")
    public ResponseEntity<MessageResponse> deleteUserFromTeam(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                              @PathVariable Long companyId,
                                                              @PathVariable Long companyTeamId ,
                                                              @PathVariable Long teamUserId) {
        MessageResponse response = companyTeamUserService.deleteUserFromTeam(customUserDetails,companyId,companyTeamId,teamUserId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/teams/{companyTeamId}/users/{userId}")
    public ResponseEntity<String> getUserInTeam(@PathVariable Long companyTeamId,
                                                                       @PathVariable Long userId) {
        String response = companyTeamUserService.getUserInTeam(companyTeamId,userId);
        return ResponseEntity.ok(response);
    }

}
