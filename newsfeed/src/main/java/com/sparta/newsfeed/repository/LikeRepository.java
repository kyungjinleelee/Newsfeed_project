package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.Like;
import com.sparta.newsfeed.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// 기능 : 좋아요 레포
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndBoard(User user, Board board);
}
