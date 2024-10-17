package com.gogetdata.company.presentation;

import com.gogetdata.company.application.CompanyUserService;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyuser.*;
import com.gogetdata.company.infrastructure.filter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyUserController {
    private final CompanyUserService companyUserService;
    @PostMapping("/{companyId}/users")
    public ResponseEntity<List<CompanyUserRegistrationResponse>> registerUserToCompany(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                                       @RequestBody List<UserRegistrationRequest> userRegistrationRequests,
                                                                                       @PathVariable Long companyId ) {
        List<CompanyUserRegistrationResponse> response = companyUserService.registerUserToCompany(customUserDetails,userRegistrationRequests,companyId);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{companyId}/users/{companyUserId}")
    public ResponseEntity<MessageResponse> deleteCompanyUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                             @PathVariable Long companyId,
                                                             @PathVariable Long companyUserId ) {
        MessageResponse response = companyUserService.deleteCompanyUser(customUserDetails,companyId,companyUserId);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{companyId}/users/{companyUserId}/type")
    public ResponseEntity<MessageResponse> updateCompanyTypeUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                             @PathVariable Long companyId,
                                                             @PathVariable Long companyUserId,
                                                             @RequestBody UpdateCompanyUserTypeRequest updateCompanyTypeRequest ) {
        MessageResponse response = companyUserService.updateCompanyTypeUser(customUserDetails,companyId,companyUserId,updateCompanyTypeRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{companyId}/users")
    public ResponseEntity<List<CompanyUserResponse>> readsCompanyUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                      @PathVariable Long companyId) {
        List<CompanyUserResponse> response = companyUserService.readsCompanyUser(customUserDetails,companyId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{companyId}/users/{companyUserId}")
    public ResponseEntity<CompanyUserResponse> readCompanyUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                      @PathVariable Long companyId,
                                                                      @PathVariable Long companyUserId) {
        CompanyUserResponse response = companyUserService.readCompanyUser(customUserDetails,companyId,companyUserId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{companyId}/users/request")
    public ResponseEntity<MessageResponse> requestCompanyUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                               @PathVariable Long companyId) {
        MessageResponse response = companyUserService.requestCompanyUser(customUserDetails,companyId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{companyId}/users/{companyUserId}/reject")
    public ResponseEntity<MessageResponse> rejectCompanyUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                              @PathVariable Long companyId,
                                                            @PathVariable Long companyUserId) {
            MessageResponse response = companyUserService.rejectCompanyUser(customUserDetails,companyId,companyUserId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{companyId}/users/requests")
    public ResponseEntity<List<CompanyWaitingUserResponse>> readsRequestCompanyUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                                    @PathVariable Long companyId) {
        List<CompanyWaitingUserResponse> response = companyUserService.readsRequestCompanyUser(customUserDetails,companyId);
        return ResponseEntity.ok(response);
    }
}
