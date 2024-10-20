package com.gogetdata.channel.presentation;

import com.gogetdata.channel.application.ChannelAccessResponse;
import com.gogetdata.channel.application.ChannelAccessService;
import com.gogetdata.channel.application.dto.MessageResponse;
import com.gogetdata.channel.application.dto.TeamRequest;
import com.gogetdata.channel.application.dto.UpdateChannelAccessRequest;
import com.gogetdata.channel.infrastructrue.filter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
@Validated
public class ChannelAccessController {

    private final ChannelAccessService channelAccessService;

    /**
     * 채널 접근 권한 부여 엔드포인트
     *
     * @param teamRequest        권한을 부여할 팀 요청 DTO
     * @param customUserDetails  인증된 사용자 정보
     * @param companyTeamId      회사 팀 ID
     * @param companyId          회사 ID
     * @param channelId          채널 ID
     * @return 성공 메시지
     */
    @PostMapping("/{channelId}/access/grant")
    public ResponseEntity<MessageResponse> grantChannelAccess(
            @PathVariable Long channelId,
            @RequestBody TeamRequest teamRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long companyTeamId,
            @RequestParam Long companyId) {

        MessageResponse response = channelAccessService.grantChannelAccess(
                teamRequest, customUserDetails, companyTeamId, companyId, channelId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 채널 접근 권한 삭제 엔드포인트
     *
     * @param channelId             채널 ID
     * @param revokeCompanyTeamId   권한을 삭제할 회사 팀 ID
     * @param customUserDetails     인증된 사용자 정보
     * @param companyTeamId         회사 팀 ID
     * @param companyId             회사 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/{channelId}/access/revoke")
    public ResponseEntity<MessageResponse> revokeChannelAccess(
            @PathVariable Long channelId,
            @RequestParam Long revokeCompanyTeamId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long companyTeamId,
            @RequestParam Long companyId) {

        MessageResponse response = channelAccessService.revokeChannelAccess(
                customUserDetails, companyTeamId, companyId, channelId, revokeCompanyTeamId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 채널 접근 권한 변경 엔드포인트
     *
     * @param channelId                 채널 ID
     * @param updateChannelAccessRequest 접근 권한 변경 요청 DTO
     * @param customUserDetails         인증된 사용자 정보
     * @param companyTeamId             회사 팀 ID
     * @param companyId                 회사 ID
     * @return 성공 메시지
     */
    @PutMapping("/{channelId}/access")
    public ResponseEntity<MessageResponse> updateChannelAccessType(
            @PathVariable Long channelId,
            @RequestBody UpdateChannelAccessRequest updateChannelAccessRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long companyTeamId,
            @RequestParam Long companyId) {

        MessageResponse response = channelAccessService.updateChannelAccessType(
                updateChannelAccessRequest, customUserDetails, companyTeamId, companyId, channelId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/{channelId}/access/{companyId}/{companyTeamId}/{channelId}")
    public ResponseEntity<List<ChannelAccessResponse>> readsChannelAccess(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long companyId,
            @PathVariable Long companyTeamId,
            @PathVariable Long channelId) {
        List<ChannelAccessResponse> response = channelAccessService.readsChannelAccess(customUserDetails, companyTeamId, companyId, channelId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
