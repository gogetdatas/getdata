package com.gogetdata.company.application.dto.feignclient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResults {
    private Long UserId;
    private Long companyUserId;
    private Boolean isSuccess;
    private String type;
    private String userName;
}
