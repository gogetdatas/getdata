package com.gogetdata.company.presentation;

import com.gogetdata.company.application.CompanyService;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.company.CompanyResponse;
import com.gogetdata.company.application.dto.company.CreateCompanyRequest;
import com.gogetdata.company.application.dto.company.UpdateCompanyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")

@RequiredArgsConstructor

public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("")
    public ResponseEntity<CompanyResponse> createCompany(@RequestHeader(value = "X-User-Id") Long userId,
                                                         @RequestHeader(value = "X-Role") String role,
                                                         @RequestBody CreateCompanyRequest createCompanyRequest) {
        CompanyResponse response = companyService.createCompany(userId,role,createCompanyRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> readCompany(@RequestHeader(value = "X-Role") String role,
                                                       @RequestHeader(value = "X-Company-Id") Long loginCompanyId,
                                                       @PathVariable Long companyId) {
        CompanyResponse response = companyService.readCompany(role,loginCompanyId,companyId);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> updateCompany(@RequestHeader(value = "X-Role") String role,
                                                         @RequestHeader(value = "X-Company-Id") Long loginCompanyId,
                                                         @RequestHeader(value = "X-Company-Role") String loginCompanyRole,
                                                         @PathVariable Long companyId , @RequestBody UpdateCompanyRequest updateCompanyRequest) {
        CompanyResponse response = companyService.updateCompany(role,companyId,updateCompanyRequest,loginCompanyId,loginCompanyRole);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{companyId}")
    public ResponseEntity<MessageResponse> deleteCompany(@RequestHeader(value = "X-Role") String role,
                                                         @RequestHeader(value = "X-Company-Id") Long loginCompanyId,
                                                         @RequestHeader(value = "X-Company-Role") String loginCompanyRole,
                                                         @PathVariable Long companyId) {
        MessageResponse response = companyService.deleteCompany(role,companyId,loginCompanyId,loginCompanyRole);
        return ResponseEntity.ok(response);
    }
}
