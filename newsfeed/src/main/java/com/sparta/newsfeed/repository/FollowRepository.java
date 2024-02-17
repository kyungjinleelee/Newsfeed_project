package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// 기능 : 팔로우 기능 레포
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query(value = "SELECT * FROM follow WHERE following_id = :followingId and follower_id = :followerId", nativeQuery = true)
    Optional<Follow> findByFollowerIdAndFollowingId(Long followingId, Long followerId);

    // 팔로우 취소
    void deleteByFollowingIdAndFollowerId(Long followingId, Long followerId);

    @Query(value = "SELECT * FROM follow WHERE follower_id = :followerId", nativeQuery = true)
    List<Follow> findByFollowerId(Long followerId);
}
