package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.Board;
import com.sparta.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByOrderByModifiedAtDesc();   // 수정된 날짜 기준 내림차순

    Optional<Board> findByIdAndUser(Long id, User user);

    void deleteAllByUser(User user);
}
