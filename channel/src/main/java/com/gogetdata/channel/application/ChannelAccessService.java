package com.gogetdata.channel.application;

import com.gogetdata.channel.application.dto.ChannelResponse;
import com.gogetdata.channel.application.dto.MessageResponse;
import com.gogetdata.channel.application.dto.TeamRequest;
import com.gogetdata.channel.application.dto.UpdateChannelAccessRequest;
import com.gogetdata.channel.domain.entity.ChannelAccess;
import com.gogetdata.channel.infrastructrue.filter.CustomUserDetails;

import java.util.List;

public interface ChannelAccessService {
    MessageResponse grantChannelAccess(TeamRequest TeamRequests,
                                       CustomUserDetails customUserDetails,
                                       Long companyTeamId,
                                       Long companyId,
                                       Long channelId); // 채널 권한 부여 // 부여할때 접근권한도 같이 부여
    MessageResponse revokeChannelAccess(CustomUserDetails customUserDetails,
                                        Long companyTeamId,
                                        Long companyId,
                                        Long channelId,
                                        Long revokeCompanyTeamId); // 채널 권한 삭제
    MessageResponse updateChannelAccessType(UpdateChannelAccessRequest updateChannelAccessRequest,
                                            CustomUserDetails customUserDetails,
                                            Long companyTeamId,
                                            Long companyId,
                                            Long channelId); // 채널 접근권한 변경
    List<ChannelAccessResponse> readsChannelAccess(CustomUserDetails customUserDetails , Long companyTeamId , Long companyId ,Long channelId);
}
