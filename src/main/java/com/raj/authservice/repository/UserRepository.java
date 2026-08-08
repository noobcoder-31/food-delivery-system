package com.raj.authservice.repository;

import com.raj.authservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    boolean existsByEmail(String email);
}
