package com.gogetdata.datamanagemant.presentation;

import com.gogetdata.datamanagemant.application.DataManagementService;
import com.gogetdata.datamanagemant.application.dto.data.QueryRequest;
import com.gogetdata.datamanagemant.application.dto.data.QueryResponse;
import com.gogetdata.datamanagemant.application.dto.types.KeySetResponse;
import com.gogetdata.datamanagemant.application.dto.types.SubTypeResponse;
import com.gogetdata.datamanagemant.application.dto.types.TypeResponse;
import com.gogetdata.datamanagemant.infrastructure.filter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/datamanagement")
@RequiredArgsConstructor

public class DataManagementController {
    private final DataManagementService dataManagementService;

    /**
     * 채널 데이터를 조회하는 엔드포인트
     *
     * @param queryRequest 클라이언트의 요청 데이터
     * @return 조회된 데이터 리스트
     */
    @PostMapping("/find/companies/{companyId}")
    public ResponseEntity<List<QueryResponse>> findChannelData(
             @RequestBody QueryRequest queryRequest,@PathVariable Long companyId) {
        List<QueryResponse> responses = dataManagementService.findChannelData(queryRequest, companyId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    /**
     * 타입 데이터를 조회하는 엔드포인트
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param companyId         회사 ID
     * @return 타입 데이터
     */
    @GetMapping("/types")
    public ResponseEntity<TypeResponse> findType(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long companyId) {
        TypeResponse response = dataManagementService.findType(customUserDetails, companyId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 서브타입 데이터를 조회하는 엔드포인트
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param typeSetId         타입 세트 ID
     * @param companyId         회사 ID
     * @return 서브타입 데이터
     */
    @GetMapping("/subtypes")
    public ResponseEntity<SubTypeResponse> findSubType(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long typeSetId,
            @RequestParam Long companyId) {
        SubTypeResponse response = dataManagementService.findSubType(customUserDetails, typeSetId, companyId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 키셋 데이터를 조회하는 엔드포인트
     *
     * @param customUserDetails 인증된 사용자 정보
     * @param subTypeSetId      서브타입 세트 ID
     * @param companyId         회사 ID
     * @return 키셋 데이터
     */
    @GetMapping("/keysets")
    public ResponseEntity<KeySetResponse> findKeySet(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long subTypeSetId,
            @RequestParam Long companyId) {
        KeySetResponse response = dataManagementService.findKeySet(customUserDetails, subTypeSetId, companyId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
