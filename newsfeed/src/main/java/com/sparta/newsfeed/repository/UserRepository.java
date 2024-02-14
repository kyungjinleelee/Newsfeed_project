package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {   //User 엔티티와 연결

    // Query method
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByKakaoId(Long kakaoId);  // 카카오 로그인

    //  Optional<User> findByUserId(Long id);
}
