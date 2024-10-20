package com.gogetdata.user.application.dto;
public record RegistrationResults(Long companyUserId, Long userId, Boolean isSuccess  , String type, String userName){
    public static RegistrationResults from(final Long companyUserId, final Long userId, final Boolean isSuccess  , final String type, final String userName) {
        return new RegistrationResults(companyUserId,userId, isSuccess  ,type,userName);
    }
}
