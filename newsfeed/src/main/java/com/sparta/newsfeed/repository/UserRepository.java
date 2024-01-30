package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {   //User 엔티티와 연결

    // Query method
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
