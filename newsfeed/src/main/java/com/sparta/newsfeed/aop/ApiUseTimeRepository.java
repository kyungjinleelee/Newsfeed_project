package com.sparta.newsfeed.aop;

import com.sparta.newsfeed.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 기능 : 회원 별 api 사용 시간 측정용 레포
public interface ApiUseTimeRepository extends JpaRepository<ApiUseTime, Long> {
    Optional<ApiUseTime> findByUser(User user);
}
