package com.gogetdata.company.application.dto.companyuser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    private Long companyUserId;
    private Long UserId;
    private String type;
}
