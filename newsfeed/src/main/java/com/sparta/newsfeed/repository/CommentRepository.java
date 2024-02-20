package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.domain.Comment;
import com.sparta.newsfeed.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


// 기능 : 댓글 정보 레포
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByBoard_IdOrderByCreatedAtDesc(Pageable pageable, Long id);

    Optional<Comment> findByIdAndUser(Long id, User user);

    @Transactional
    void deleteAllByUser(User user);
    boolean existsByUser(User user);

    //  Optional<Comment> findByIdAndUser(Long id, User user);
}
