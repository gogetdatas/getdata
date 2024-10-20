package com.gogetdata.channel.infrastructrue;

import com.gogetdata.channel.application.DataManagementService;
import com.gogetdata.channel.application.dto.ChannelDataResponse;
import com.gogetdata.channel.infrastructrue.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@FeignClient(name = "datamanagement-service",
            configuration =  FeignClientConfig.class)

public interface DataManagementClient extends DataManagementService {
    @GetMapping("/datamanagement/find/companies/{companyId}/channels/{channelId}")
    List<ChannelDataResponse> findChannelData(@PathVariable(value = "companyId") Long companyId,@PathVariable(value = "channelId") Long channelId);
}
