package com.gogetdata.company.infrastructure;

import com.gogetdata.company.application.UserService;
import com.gogetdata.company.application.dto.companyuser.UserRegistration;
import com.gogetdata.company.application.dto.feignclient.MyInfoResponse;
import com.gogetdata.company.application.dto.feignclient.RegistrationResult;
import com.gogetdata.company.application.dto.feignclient.RegistrationResults;
import jakarta.ws.rs.POST;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserClient extends UserService {
    @PostMapping("/user/companies/registers") // 유저 검증 API
    List<RegistrationResults> registerUsers(@RequestBody UserRegistration userRegistration);
    @PutMapping("/user/companies/delete/{userId}") // 유저 검증 API
    Boolean deleteCompanyUser(@PathVariable(value = "userId")Long userId);
    @GetMapping("/user/companies/verify/{userId}")
    RegistrationResult checkUser(@PathVariable(value = "userId")Long userId);
    @GetMapping("/user/{userId}")
    MyInfoResponse getMyInfo(@PathVariable(value = "userId") Long userId);
    @GetMapping("/user/{userId}/companies/register") // 유저 검증 API
    RegistrationResult registerUser(@PathVariable(value = "userId") Long userId , @RequestParam(value = "companyId") Long companyId);
}