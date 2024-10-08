package com.gogetdata.company.application.dto;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)

public class MessageResponse {
    private String message;
    public static MessageResponse from(String message) {
        return MessageResponse.builder().message(message).build();

    }

}
