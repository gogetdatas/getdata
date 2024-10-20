package com.gogetdata.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistration {
    private Long companyId;
    private List<UserRegistrationRequest> userRegistrationRequestList = new ArrayList<>();
}
