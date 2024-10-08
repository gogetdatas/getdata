package com.gogetdata.company.presentation;

import com.gogetdata.company.application.CompanyService;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.company.CompanyResponse;
import com.gogetdata.company.application.dto.company.CreateCompanyRequest;
import com.gogetdata.company.application.dto.company.UpdateCompanyRequest;
import com.gogetdata.company.infrastructure.filter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")

@RequiredArgsConstructor

public class CompanyController {
    private final CompanyService companyService;
    @PostMapping("")
    public ResponseEntity<CompanyResponse> createCompany(@AuthenticationPrincipal CustomUserDetails userDetails , @RequestBody CreateCompanyRequest createCompanyRequest) {
        CompanyResponse response = companyService.createCompany(userDetails,createCompanyRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> readCompany(@AuthenticationPrincipal CustomUserDetails userDetails , @PathVariable Long companyId) {
        CompanyResponse response = companyService.readCompany(userDetails,companyId);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> updateCompany(@AuthenticationPrincipal CustomUserDetails userDetails , @PathVariable Long companyId , @RequestBody UpdateCompanyRequest updateCompanyRequest) {
        CompanyResponse response = companyService.updateCompany(userDetails,companyId,updateCompanyRequest);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{companyId}")
    public ResponseEntity<MessageResponse> deleteCompany(@AuthenticationPrincipal CustomUserDetails userDetails , @PathVariable Long companyId) {
        MessageResponse response = companyService.deleteCompany(companyId,userDetails);
        return ResponseEntity.ok(response);
    }
}
