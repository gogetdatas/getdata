package com.gogetdata.auth.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class signInRequest {
    String email;
    String password;
}
