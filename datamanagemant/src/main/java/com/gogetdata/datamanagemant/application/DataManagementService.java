package com.gogetdata.datamanagemant.application;

import com.gogetdata.datamanagemant.application.dto.data.QueryRequest;
import com.gogetdata.datamanagemant.application.dto.data.QueryResponse;
import com.gogetdata.datamanagemant.application.dto.types.KeySetResponse;
import com.gogetdata.datamanagemant.application.dto.types.SubTypeResponse;
import com.gogetdata.datamanagemant.application.dto.types.TypeResponse;
import com.gogetdata.datamanagemant.infrastructure.filter.CustomUserDetails;

import java.util.List;

public interface DataManagementService {
    List<QueryResponse> findChannelData(QueryRequest queryRequest,Long companyId); // 데이터 조회
    TypeResponse findType(CustomUserDetails customUserDetails , Long companyId);// type 조회
    SubTypeResponse findSubType(CustomUserDetails customUserDetails, Long typeSetId, Long companyId);// subtype 조회
    KeySetResponse findKeySet(CustomUserDetails customUserDetails, Long subTypeSetId, Long companyId);// keyset 조회 // type , subtype keyset은 데이터관리 서비스에서 하는게 나을듯

}
