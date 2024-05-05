package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// 기능 : 보드 정보 레포
@RepositoryDefinition(domainClass = Board.class, idClass = Long.class)
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    Page<Board> findAll(Pageable pageable);

    List<Board> findAllByOrderByModifiedAtDesc();   // 수정된 날짜 기준 내림차순

    Optional<Board> findByIdAndUser(Long id, User user);

    @Transactional
    void deleteAllByUser(User user);    // 해당 유저가 작성한 글 모두 삭제
    boolean existsByUser(User user);    // 해당 유저가 작성한 글 존재여부 확인

    // 팔로우 한 유저 글 보기 관련
    List<Board> findByUserIn(List<User> followings);

    // 내가 쓴 글 보기
    List<Board> findByUserIdOrderByCreatedAtDesc(Long userid);
}
