package com.gogetdata.company.application.dto.feignclient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyInfoResponse {
    Long userId;
    String userName;
    String email;
    boolean isApprove;
}
