package com.gogetdata.company.application.dto.companyteamuser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserPerMissionRequest {
    Long companyTeamUserId;
    String type;
}
