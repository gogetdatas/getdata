package com.gogetdata.channel.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "channel")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class Channel extends BaseEntity{
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Long channelId;
    @Column(name = "channel_name")
    private String channelName;
    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public static Channel create(String channelName) {
        return Channel.builder()
                .channelName(channelName)
                .build();
    }

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     * ex (update 메소드)
     */
    public void delete(){
        super.Delete();
    }
    public void updateName(String channelName){
        this.channelName = channelName;
    }

}
