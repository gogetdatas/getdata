package com.gogetdata.company.infrastructure;

import com.gogetdata.company.application.UserService;
import com.gogetdata.company.application.dto.companyuser.UserRegistration;
import com.gogetdata.company.application.dto.feignclient.MyInfoResponse;
import com.gogetdata.company.application.dto.feignclient.RegistrationResult;
import com.gogetdata.company.application.dto.feignclient.RegistrationResults;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserClient extends UserService {
    @GetMapping("/companies/registers") // 유저 검증 API
    List<RegistrationResults> registerUsers(@RequestBody UserRegistration userRegistration);
    @PutMapping("/companies/delete/{userId}") // 유저 검증 API
    Boolean deleteCompanyUser(@PathVariable Long userId);
    @GetMapping("/companies/verify/{userId}")
    RegistrationResult checkUser(@PathVariable Long userId);
    @GetMapping("/{userId}")
    MyInfoResponse getMyInfo(@PathVariable Long userId);
    @GetMapping("/{userId}/companies/register") // 유저 검증 API
    RegistrationResult registerUser(@PathVariable Long userId , @RequestParam Long companyId);
}