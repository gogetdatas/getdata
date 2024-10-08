package com.gogetdata.company.application.dto.feignclient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResult {
    Long userId;
    String userName;
    String userEmail;
}
