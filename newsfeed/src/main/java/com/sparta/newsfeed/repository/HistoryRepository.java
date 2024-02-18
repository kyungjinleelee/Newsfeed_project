package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.domain.PwdHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// 기능 : 비밀번호 history 레포
@Repository
public interface HistoryRepository extends JpaRepository<PwdHistory, Long> {

    List<PwdHistory> findByUserId(Long id);
}
