package com.gogetdata.company.application;

import com.gogetdata.company.application.dto.companyuser.UserRegistration;
import com.gogetdata.company.application.dto.feignclient.MyInfoResponse;
import com.gogetdata.company.application.dto.feignclient.RegistrationResult;
import com.gogetdata.company.application.dto.feignclient.RegistrationResults;

import java.util.List;

public interface UserService {
    List<RegistrationResults> registerUsers(UserRegistration userRegistration);
    Boolean deleteCompanyUser(Long userId);
    RegistrationResult checkUser(Long userId);
    MyInfoResponse readUser(Long userId);
    RegistrationResult registerUser(Long userId,Long companyId);
}
