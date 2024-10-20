package com.gogetdata.channel.infrastructrue;

import com.gogetdata.channel.application.CompanyTeamService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service")
public interface CompanyTeamClient extends CompanyTeamService {
    @GetMapping("/company/teams/{companyTeamId}/users/{userId}") // 유저 검증 API
    String checkUserInTeam(@PathVariable Long companyTeamId,@PathVariable Long userId);
}