package com.gogetdata.datamanagemant.domain.repository;

import com.gogetdata.datamanagemant.application.dto.data.QueryRequest;
import com.gogetdata.datamanagemant.application.dto.data.QueryResponse;

import java.util.List;

public interface CompanyDataRepositoryCustom {
    List<QueryResponse> findChannelData(QueryRequest queryRequest,Long companyId);

}
