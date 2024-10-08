package com.gogetdata.company.presentation;

import com.gogetdata.company.application.CompanyUserService;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.companyuser.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyUserController {
    private final CompanyUserService companyUserService; //  todo :  로그인한 유저가 속한 회사를 찾는 API
    @PostMapping("/{companyId}/users")
    public ResponseEntity<List<CompanyUserRegistrationResponse>> registerUserToCompany(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                                                       @RequestHeader(value = "X-Role") String role,
                                                                                       @RequestBody List<UserRegistrationRequest> userRegistrationRequests,
                                                                                       @PathVariable Long companyId ) {
        List<CompanyUserRegistrationResponse> response = companyUserService.registerUserToCompany(loginUserId,role,userRegistrationRequests,companyId);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{companyId}/users/{companyUserId}")
    public ResponseEntity<MessageResponse> deleteCompanyUser(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                             @RequestHeader(value = "X-Role") String role,
                                                             @PathVariable Long companyId,
                                                             @PathVariable Long companyUserId ) {
        MessageResponse response = companyUserService.deleteCompanyUser(loginUserId,role,companyId,companyUserId);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{companyId}/users/{companyUserId}/type")
    public ResponseEntity<MessageResponse> updateCompanyTypeUser(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                                 @RequestHeader(value = "X-Role") String role,
                                                             @PathVariable Long companyId,
                                                             @PathVariable Long companyUserId,
                                                             @RequestBody UpdateCompanyUserTypeRequest updateCompanyTypeRequest ) {
        MessageResponse response = companyUserService.updateCompanyTypeUser(loginUserId,role,companyId,companyUserId,updateCompanyTypeRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{companyId}/users")
    public ResponseEntity<List<CompanyUserResponse>> readsCompanyUser(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                                      @RequestHeader(value = "X-Role") String role,
                                                                      @PathVariable Long companyId) {
        List<CompanyUserResponse> response = companyUserService.readsCompanyUser(loginUserId,role,companyId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{companyId}/users/{companyUserId}")
    public ResponseEntity<CompanyUserResponse> readCompanyUser(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                               @RequestHeader(value = "X-Role") String role,
                                                                      @PathVariable Long companyId,
                                                                      @PathVariable Long companyUserId) {
        CompanyUserResponse response = companyUserService.readCompanyUser(loginUserId,role,companyId,companyUserId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{companyId}/users/request")
    public ResponseEntity<MessageResponse> requestCompanyUser(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                              @RequestHeader(value = "X-Role") String role,
                                                               @PathVariable Long companyId) {
        MessageResponse response = companyUserService.requestCompanyUser(loginUserId,role,companyId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{companyId}/users/{companyUserId}/reject")
    public ResponseEntity<MessageResponse> rejectCompanyUser(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                             @RequestHeader(value = "X-Role") String role,
                                                              @PathVariable Long companyId,
                                                            @PathVariable Long companyUserId) {
            MessageResponse response = companyUserService.rejectCompanyUser(loginUserId,role,companyId,companyUserId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{companyId}/users/requests")
    public ResponseEntity<List<CompanyWaitingUserResponse>> readsRequestCompanyUser(@RequestHeader(value = "X-User-Id") Long loginUserId,
                                                                                    @RequestHeader(value = "X-Role") String role,
                                                                                    @PathVariable Long companyId) {
        List<CompanyWaitingUserResponse> response = companyUserService.readsRequestCompanyUser(loginUserId,role,companyId);
        return ResponseEntity.ok(response);
    }
}
