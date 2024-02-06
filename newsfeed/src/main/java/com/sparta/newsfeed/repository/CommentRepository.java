package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.Comment;
import com.sparta.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndUser(Long id, User user);
}
