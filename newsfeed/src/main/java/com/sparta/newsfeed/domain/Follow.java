package com.sparta.newsfeed.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 기능 : 팔로우 기능 Entity
@Entity
@NoArgsConstructor
@Getter
@Table(name = "follow")
public class Follow extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="follow_id")
    private Long followId;

    @ManyToOne
    @JoinColumn(name = "follower_id")           // 현재 유저 (팔로우를 하는 사람)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_id")      // 팔로우 당하는 사람
    private User following;

//    public Follow(User following, User follower) {
//        this.following = following;
//        this.follower = follower;
//    }

    @Builder
    public Follow(Long followId, User following, User follower) {
        this.followId = followId;
        this.following = following;
        this.follower = follower;
    }

    public static Follow createFollow(User following, User follower) {
        return Follow.builder()
                .following(following)
                .follower(follower)
                .build();
    }


}
