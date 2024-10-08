package com.gogetdata.company.infrastructure;

import com.gogetdata.company.application.UserService;
import com.gogetdata.company.application.dto.feignclient.MyInfoResponse;
import com.gogetdata.company.application.dto.companyuser.UserRegistrationRequest;
import com.gogetdata.company.application.dto.feignclient.RegistrationResult;
import com.gogetdata.company.application.dto.feignclient.RegistrationResults;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserClient extends UserService {
    @GetMapping("/company/registers") // 유저 검증 API
    List<RegistrationResults> registerUsers(@RequestParam List<UserRegistrationRequest> userRegistrationRequests);
    @PutMapping("/company/delete/{userId}") // 유저 검증 API
    Boolean deleteCompanyUser(@PathVariable Long userId);
    @GetMapping("/company/verify/{userId}")
    Boolean checkUser(@PathVariable Long userId);
    @GetMapping("/{userId}")
    MyInfoResponse getMyInfo(@PathVariable Long userId);
    @GetMapping("/company/register/{userId}") // 유저 검증 API
    RegistrationResult registrationUser(@PathVariable Long userId);
}