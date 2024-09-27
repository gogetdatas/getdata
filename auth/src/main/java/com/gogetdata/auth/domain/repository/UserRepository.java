package com.gogetdata.auth.domain.repository;

import com.gogetdata.auth.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByemail(String email);
}
