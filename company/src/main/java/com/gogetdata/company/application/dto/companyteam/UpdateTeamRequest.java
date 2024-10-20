package com.gogetdata.company.application.dto.companyteam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateTeamRequest {
    String teamName;
}
