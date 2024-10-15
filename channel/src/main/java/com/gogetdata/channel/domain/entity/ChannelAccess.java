package com.gogetdata.channel.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "channelaccess")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class ChannelAccess extends BaseEntity{
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channelaccess_id")
    private Long channelAccessId;
    @Column(name = "channel_id")
    private Long channelId;
    @Column(name = "company_team_id")
    private Long companyTeamId;
    @Enumerated(EnumType.STRING)
    @Column(name = "channel_access_type", nullable = false)
    private ChannelAccessType channelAccessType;
    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public static ChannelAccess create(Long channelId,Long companyTeamId,ChannelAccessType channelAccessType) {
        return ChannelAccess.builder()
                .channelId(channelId)
                .companyTeamId(companyTeamId)
                .channelAccessType(channelAccessType)
                .build();
    }
    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */
    public void delete(){
        super.Delete();
    }
    public void updateType(String Type){
        this.channelAccessType = ChannelAccessType.valueOf(Type);
    }
}
