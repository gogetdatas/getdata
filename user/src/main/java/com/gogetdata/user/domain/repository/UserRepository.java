package com.gogetdata.user.domain.repository;

import com.gogetdata.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

}
