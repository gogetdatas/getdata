package com.gogetdata.user.application.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class DeleteUserResponse {
    private String delete;
        public static DeleteUserResponse from(String delete) {
            return DeleteUserResponse.builder().delete(delete).build();
        }
}
