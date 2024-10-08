package com.gogetdata.auth.application.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter
public class signUpRequest {
    @NotBlank(message = "이름을 입력")
    @Size(min = 4, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]{4,10}$")
    private String userName;

    @NotBlank(message = "비밀번호 입력")
    @Size(min = 8, max = 15)
    @Pattern(regexp = "^[a-zA-Z0-9_#$%^!-]{8,15}$")
    private String password;

    @NotBlank(message = "이메일 입력")
    @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
    private String email;
}
