package com.gogetdata.channel.application;

public interface ChannelService {
    void createChannel();// 채널 생성
    void deleteChannel();// 채널 삭제
    void updateChannel();// 채널 수정
    void getChannel(); // 채널 조회
    void listChannels(); // 가지고 있는 채널 목록    조회
    void grantChannelAccess(); // 채널 권한 부여
    void revokeChannelAccess(); // 채널 권한 삭제
    void createChannelSetting();// 채널 설정 생성
    void updateChannelSetting();// 채널 설정 수정
    void getChannelSetting(); // 채널 설정 조회
    void deleteChannelSetting();// 채널 설정 삭제
//    void readType();// type 조회
//    void readSubType();// subtype 조회
//    void readKeySet();// keyset 조회 // type , subtype keyset은 데이터관리 서비스에서 하는게 나을듯
}
