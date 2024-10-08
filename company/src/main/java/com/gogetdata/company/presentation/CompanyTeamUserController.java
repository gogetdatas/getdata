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
    public ResponseEntity<MessageResponse> applyToJoinTeam(@AuthenticationPrincipal CustomUserDetails userDetails ,
                                                              @PathVariable Long companyId,
                                                              @PathVariable Long companyTeamId ) {
        MessageResponse response = companyTeamUserService.applyToJoinTeam(userDetails,companyId,companyTeamId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{companyId}/teams/{companyTeamId}/users")
    public ResponseEntity<List<MessageResponse>> acceptJoinRequest(@AuthenticationPrincipal CustomUserDetails userDetails ,
                                                                   @PathVariable Long companyId,
                                                                   @PathVariable Long companyTeamId ,
                                                                   @RequestBody List<AcceptJoinRequest> acceptJoinRequest) {
        List<MessageResponse> response = companyTeamUserService.acceptJoinRequest(userDetails,companyId,companyTeamId,acceptJoinRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{companyId}/teams/{companyTeamId}/users/{teamUserId}/reject")
    public ResponseEntity<MessageResponse> rejectJoinRequest(@AuthenticationPrincipal CustomUserDetails userDetails ,
                                                                   @PathVariable Long companyId,
                                                                   @PathVariable Long companyTeamId ,
                                                                   @PathVariable Long teamUserId) {
        MessageResponse response = companyTeamUserService.rejectJoinRequest(userDetails,companyId,companyTeamId,teamUserId);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{companyId}/teams/{companyTeamId}/users/{teamUserId}/permission")
    public ResponseEntity<MessageResponse> updateUserPermission(@AuthenticationPrincipal CustomUserDetails userDetails ,
                                                             @PathVariable Long companyId,
                                                             @PathVariable Long companyTeamId ,
                                                             @PathVariable Long teamUserId,
                                                             @RequestBody UpdateUserPerMissionRequest updateUserPerMissionRequest) {
        MessageResponse response = companyTeamUserService.updateUserPermission(userDetails,companyId,companyTeamId,teamUserId,updateUserPerMissionRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/me/teams")
    public ResponseEntity<List<CompanyTeamResponse>> getMyTeams(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<CompanyTeamResponse> response = companyTeamUserService.getMyTeams(userDetails);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/teams/{companyTeamId}/users")
    public ResponseEntity<List<CompanyTeamUserResponse>> getUsersInTeam(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @PathVariable Long companyTeamId) {
        List<CompanyTeamUserResponse> response = companyTeamUserService.getUsersInTeam(userDetails,companyTeamId);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{companyId}/teams/{companyTeamId}/users/{teamUserId}")
    public ResponseEntity<MessageResponse> deleteUserFromTeam(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                            @PathVariable Long companyId,
                                                                            @PathVariable Long companyTeamId ,
                                                                            @PathVariable Long teamUserId) {
        MessageResponse response = companyTeamUserService.deleteUserFromTeam(userDetails,companyId,companyTeamId,teamUserId);
        return ResponseEntity.ok(response);
    }
}
