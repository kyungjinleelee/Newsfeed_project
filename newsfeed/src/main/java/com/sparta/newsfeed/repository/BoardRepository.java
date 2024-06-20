package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;
import java.util.Optional;

// 기능 : 보드 정보 레포
@RepositoryDefinition(domainClass = Board.class, idClass = Long.class)
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    @Query("SELECT p FROM Board p JOIN FETCH p.user")
    Page<Board> findAll(Pageable pageable);

    Optional<Board> findByIdAndUser(Long id, User user);


    // 팔로우 한 유저 글 보기 관련
    List<Board> findByUserIn(List<User> followings);

    // 내가 쓴 글 보기
    List<Board> findByUserIdOrderByCreatedAtDesc(Long userid);
}
