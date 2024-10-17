package com.gogetdata.channel.domain.service;

import com.gogetdata.channel.application.ChannelAccessResponse;
import com.gogetdata.channel.application.ChannelAccessService;
import com.gogetdata.channel.application.CompanyTeamService;
import com.gogetdata.channel.application.dto.MessageResponse;
import com.gogetdata.channel.application.dto.TeamRequest;
import com.gogetdata.channel.application.dto.UpdateChannelAccessRequest;
import com.gogetdata.channel.domain.entity.ChannelAccess;
import com.gogetdata.channel.domain.entity.ChannelAccessType;
import com.gogetdata.channel.domain.repository.ChannelAccessRepository;
import com.gogetdata.channel.infrastructrue.filter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChannelAccessServiceImpl implements ChannelAccessService {
    private final ChannelAccessRepository channelAccessRepository;
    @Override
    @Transactional
    public MessageResponse grantChannelAccess(TeamRequest teamRequests,
                                              CustomUserDetails customUserDetails,
                                              Long companyTeamId,
                                              Long companyId,
                                              Long channelId) {
        getAccessible(customUserDetails , companyId,companyTeamId,channelId);
        List<ChannelAccess> channelAccesses = new ArrayList<>();
        for (Long teamId :  teamRequests.getCompanyTeamId()) {
            ChannelAccess channelAccess = ChannelAccess.create(channelId,teamId, ChannelAccessType.READ);
            channelAccesses.add(channelAccess);
        }
        channelAccessRepository.saveAll(channelAccesses);
        return MessageResponse.from("추가");
    }
    @Override
    @Transactional
    public MessageResponse revokeChannelAccess(
            CustomUserDetails customUserDetails,
            Long companyTeamId,
            Long companyId,
            Long channelId,
            Long revokeCompanyTeamId) {
        getAccessible(customUserDetails , companyId,companyTeamId,channelId);
        ChannelAccess channelAccess=channelAccessRepository.findAccessChannel(revokeCompanyTeamId,channelId);
        channelAccess.delete();
        channelAccessRepository.save(channelAccess);

        return MessageResponse.from("권한취소");
    }
    @Override
    @Transactional
    public MessageResponse updateChannelAccessType(
            UpdateChannelAccessRequest updateChannelAccessRequest,
            CustomUserDetails customUserDetails,
            Long companyTeamId,
            Long companyId,
            Long channelId) {
        getAccessible(customUserDetails , companyId,companyTeamId,channelId);
        ChannelAccess channelAccess=channelAccessRepository.findAccessChannel(updateChannelAccessRequest.getCompanyTeamId(),channelId);
        channelAccess.updateType(updateChannelAccessRequest.getUpdateType());
        channelAccessRepository.save(channelAccess);
        return MessageResponse.from("권한변경");
    }

    @Override
    public List<ChannelAccessResponse> readsChannelAccess(CustomUserDetails customUserDetails, Long companyTeamId, Long companyId, Long channelId) {
        getAccessible(customUserDetails , companyId,companyTeamId,channelId);
        List<ChannelAccessResponse> channelAccessResponses = new ArrayList<>();
        List<ChannelAccess> channelAccesses=channelAccessRepository.findsAccessChannel(channelId);
        for (ChannelAccess channelAccess : channelAccesses) {
            channelAccessResponses.add(ChannelAccessResponse.from(channelAccess));
        }
        return channelAccessResponses;
    }

    private void getAccessible(CustomUserDetails customUserDetails, Long companyId,Long companyTeamId ,Long channelId) {
        if (isAdmin(customUserDetails.getAuthorities().toString())) {
            return;
        }
        if(!isCompanyAdmin(customUserDetails.companyId(),companyId,customUserDetails.getCompanyType())) {
            getAccessibleAdminChannel(companyTeamId,channelId);
        }

    }
    private void getAccessibleAdminChannel(Long companyTeamId,Long channelId) {
        if(!channelAccessRepository.findAccessChannel(companyTeamId,channelId).getChannelAccessType().getAuthority().equals("ADMIN")){
            throw new IllegalAccessError("권한없음");
        }
    }

    private boolean isAdmin(String role) {
        return role.equals("ADMIN");
    }
    private boolean isCompanyAdmin(Long companyId, Long loginCompanyId, String companyType) {
        if (Objects.equals(loginCompanyId, companyId)) {
            return companyType.equals("ADMIN");
        }
        return false;
    }
}
