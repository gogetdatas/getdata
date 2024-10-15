package com.gogetdata.channel.infrastructrue;

import com.gogetdata.channel.application.DataManagementService;
import com.gogetdata.channel.application.dto.ChannelDataResponse;
import com.gogetdata.channel.application.dto.QueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@FeignClient(name = "datamanagement-service")

public interface DataManagementClient extends DataManagementService {
    @GetMapping("/datamanagement/find/companies/{companyId}")
    List<ChannelDataResponse> findChannelData(@RequestBody QueryRequest request , @PathVariable Long companyId);
}
