package com.gogetdata.auth.domain.repository;

import com.gogetdata.auth.domain.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    UserInfo findByUserId(Long userId);
}
