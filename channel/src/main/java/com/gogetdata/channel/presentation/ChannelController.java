package com.gogetdata.channel.presentation;

import com.gogetdata.channel.application.ChannelService;
import com.gogetdata.channel.application.dto.*;
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
public class ChannelController {
    private final ChannelService channelService;
    /**
     * 채널 유형만 생성하는 엔드포인트
     *
     * @param createChannelRequest 요청 DTO
     * @param customUserDetails    인증된 사용자 정보
     * @param companyTeamId        회사 팀 ID
     * @param companyId            회사 ID
     * @return 성공 메시지
     */
    @PostMapping("/type-only/{companyId}/{companyTeamId}")
    public ResponseEntity<MessageResponse> createChannelTypeOnly(
            @RequestBody CreateChannelTypeOnlyRequest createChannelRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long companyTeamId,
            @PathVariable Long companyId) {

        MessageResponse response = channelService.createChannelTypeOnly(
                createChannelRequest, customUserDetails, companyTeamId, companyId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 서브타입을 포함한 채널 생성 엔드포인트
     *
     * @param createChannelRequest 요청 DTO
     * @param customUserDetails    인증된 사용자 정보
     * @param companyTeamId        회사 팀 ID
     * @param companyId            회사 ID
     * @return 성공 메시지
     */
    @PostMapping("/with-subtype/{companyId}/{companyTeamId}")
    public ResponseEntity<MessageResponse> createChannelWithSubtype(
            @RequestBody CreateChannelWithSubtypeRequest createChannelRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long companyTeamId,
            @PathVariable Long companyId) {

        MessageResponse response = channelService.createChannelWithSubtype(
                createChannelRequest, customUserDetails, companyTeamId, companyId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 집계를 포함한 채널 생성 엔드포인트
     *
     * @RequestBody createChannelRequest 요청 DTO
     * @AuthenticationPrincipal customUserDetails    인증된 사용자 정보
     * @PathVariable companyTeamId        회사 팀 ID
     * @PathVariable companyId            회사 ID
     * @return 성공 메시지
     */
    @PostMapping("/with-aggregation/{companyId}/{companyTeamId}")
    public ResponseEntity<MessageResponse> createChannelWithAggregation(
            @RequestBody CreateChannelWithAggregationRequest createChannelRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long companyTeamId,
            @PathVariable Long companyId) {

        MessageResponse response = channelService.createChannelWithAggregation(
                createChannelRequest, customUserDetails, companyTeamId, companyId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 채널 삭제 엔드포인트
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param companyTeamId     회사 팀 ID
     * @param companyId         회사 ID
     * @param channelId         삭제할 채널 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/{channelId}")
    public ResponseEntity<MessageResponse> deleteChannel(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long companyTeamId,
            @RequestParam Long companyId,
            @PathVariable Long channelId) {

        MessageResponse response = channelService.deleteChannel(
                customUserDetails, companyTeamId, companyId, channelId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 채널 이름 수정 엔드포인트
     *
     * @param updateChannelNameRequest 요청 DTO
     * @param customUserDetails        인증된 사용자 정보
     * @param companyTeamId            회사 팀 ID
     * @param companyId                회사 ID
     * @param channelId                수정할 채널 ID
     * @return 성공 메시지
     */
    @PutMapping("/{channelId}")
    public ResponseEntity<MessageResponse> updateChannel(
            @Valid @RequestBody UpdateChannelNameRequest updateChannelNameRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long companyTeamId,
            @RequestParam Long companyId,
            @PathVariable Long channelId) {

        MessageResponse response = channelService.updateChannel(
                updateChannelNameRequest, customUserDetails, companyTeamId, companyId, channelId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 채널 조회 엔드포인트
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param companyTeamId     회사 팀 ID
     * @param companyId         회사 ID
     * @param channelId         조회할 채널 ID
     * @return 채널 데이터 리스트
     */
    @GetMapping("/companies/{companyId}/teams/{companyTeamId}/{channelId}")
    public ResponseEntity<List<ChannelDataResponse>> getChannel(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long companyId,
            @PathVariable Long companyTeamId,
            @PathVariable Long channelId) {

        List<ChannelDataResponse> responses = channelService.getChannel(
                customUserDetails, companyTeamId, companyId, channelId);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    /**
     * 채널 목록 조회 엔드포인트
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param companyId         회사 ID
     * @param companyTeamIds    조회할 회사 팀 ID 리스트
     * @return 채널 목록 응답
     */
    @GetMapping
    public ResponseEntity<ChannelListResponse> listChannels(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long companyId,
            @RequestParam List<Long> companyTeamIds) {

        ChannelListResponse response = channelService.listChannels(
                customUserDetails, companyId, companyTeamIds);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 채널 설정 수정 엔드포인트
     *
     * @param channelId       수정할 채널 ID
     * @param updateRequest   요청 DTO
     * @param customUserDetails 인증된 사용자 정보
     * @param companyTeamId      회사 팀 ID
     * @param companyId          회사 ID
     * @return 성공 메시지
     */
    @PutMapping("/{channelId}/setting")
    public ResponseEntity<MessageResponse> updateChannelSetting(
            @PathVariable Long channelId,
            @RequestBody UpdateChannelWithAggregationRequest updateRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long companyTeamId,
            @RequestParam Long companyId) {

        MessageResponse response = channelService.updateChannelSetting(
                channelId, updateRequest, customUserDetails, companyTeamId, companyId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 채널 설정 조회 엔드포인트
     *
     * @param channelId         조회할 채널 ID
     * @param customUserDetails 인증된 사용자 정보
     * @param companyTeamId     회사 팀 ID
     * @param companyId         회사 ID
     * @return 채널 설정 응답
     */
    @GetMapping("/{channelId}/setting")
    public ResponseEntity<ChannelSettingResponse> getChannelSetting(
            @PathVariable Long channelId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long companyTeamId,
            @RequestParam Long companyId) {

        ChannelSettingResponse response = channelService.getChannelSetting(
                channelId, customUserDetails, companyTeamId, companyId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
