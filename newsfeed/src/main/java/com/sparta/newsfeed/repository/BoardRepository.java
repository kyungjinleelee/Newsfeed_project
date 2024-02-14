package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// 기능 : 보드 정보 레포
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByOrderByModifiedAtDesc();   // 수정된 날짜 기준 내림차순

    Optional<Board> findByIdAndUser(Long id, User user);

    @Transactional
    void deleteAllByUser(User user);    // 해당 유저가 작성한 글 모두 삭제
    boolean existsByUser(User user);    // 해당 유저가 작성한 글 존재여부 확인
}
