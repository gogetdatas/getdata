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
    private final CompanyUserService companyUserService; //  todo :  로그인한 유저가 속한 회사를 찾는 API
    @PostMapping("/{companyId}/users")
    public ResponseEntity<List<CompanyUserRegistrationResponse>> registerUserToCompany(@AuthenticationPrincipal CustomUserDetails userDetails ,
                                                                                       @RequestBody List<UserRegistrationRequest> userRegistrationRequests,
                                                                                       @PathVariable Long companyId ) {
        List<CompanyUserRegistrationResponse> response = companyUserService.registerUserToCompany(userDetails,userRegistrationRequests,companyId);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{companyId}/users/{companyUserId}")
    public ResponseEntity<MessageResponse> deleteCompanyUser(@AuthenticationPrincipal CustomUserDetails userDetails ,
                                                             @PathVariable Long companyId,
                                                             @PathVariable Long companyUserId ) {
        MessageResponse response = companyUserService.deleteCompanyUser(userDetails,companyId,companyUserId);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{companyId}/users/{companyUserId}/type")
    public ResponseEntity<MessageResponse> updateCompanyTypeUser(@AuthenticationPrincipal CustomUserDetails userDetails ,
                                                             @PathVariable Long companyId,
                                                             @PathVariable Long companyUserId,
                                                             @RequestBody UpdateCompanyUserTypeRequest updateCompanyTypeRequest ) {
        MessageResponse response = companyUserService.updateCompanyTypeUser(userDetails,companyId,companyUserId,updateCompanyTypeRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{companyId}/users")
    public ResponseEntity<List<CompanyUserResponse>> readsCompanyUser(@AuthenticationPrincipal CustomUserDetails userDetails ,
                                                                      @PathVariable Long companyId) {
        List<CompanyUserResponse> response = companyUserService.readsCompanyUser(userDetails,companyId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{companyId}/users/{companyUserId}")
    public ResponseEntity<CompanyUserResponse> readCompanyUser(@AuthenticationPrincipal CustomUserDetails userDetails ,
                                                                      @PathVariable Long companyId,
                                                                      @PathVariable Long companyUserId) {
        CompanyUserResponse response = companyUserService.readCompanyUser(userDetails,companyId,companyUserId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{companyId}/users/request")
    public ResponseEntity<MessageResponse> requestCompanyUser(@AuthenticationPrincipal CustomUserDetails userDetails ,
                                                               @PathVariable Long companyId) {
        MessageResponse response = companyUserService.requestCompanyUser(userDetails,companyId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{companyId}/users/{companyUserId}/reject")
    public ResponseEntity<MessageResponse> rejectCompanyUser(@AuthenticationPrincipal CustomUserDetails userDetails ,
                                                              @PathVariable Long companyId,
                                                            @PathVariable Long companyUserId) {
            MessageResponse response = companyUserService.rejectCompanyUser(userDetails,companyId,companyUserId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{companyId}/users/requests")
    public ResponseEntity<List<CompanyWaitingUserResponse>> readsRequestCompanyUser(@AuthenticationPrincipal CustomUserDetails userDetails ,
                                                                                    @PathVariable Long companyId) {
        List<CompanyWaitingUserResponse> response = companyUserService.readsRequestCompanyUser(userDetails,companyId);
        return ResponseEntity.ok(response);
    }
}
