package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.feignclient.MyInfoResponse;
import com.gogetdata.company.application.dto.companyuser.UserRegistrationRequest;
import com.gogetdata.company.application.dto.feignclient.RegistrationResult;
import com.gogetdata.company.application.dto.feignclient.RegistrationResults;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserService {
    List<RegistrationResults> registerUsers(List<UserRegistrationRequest> userRegistrationRequests);
    Boolean deleteCompanyUser(Long userId);
    Boolean checkUser(@PathVariable Long userId);
    MyInfoResponse readUser(Long userId);
    RegistrationResult registerUser(Long userId);
}
