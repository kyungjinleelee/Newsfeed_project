package com.sparta.newsfeed.dto.RequestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.newsfeed.domain.Follow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 기능 : 팔로우 기능 요청 Dto
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FollowRequestDto {

    @JsonProperty("follow_id")
    private Long followId;

    @JsonProperty("following_id")
    private Long following_id;

    @JsonProperty("follower_id")
    private Long follower_id;

    public static FollowRequestDto requestDto(Follow follow) {
        return new FollowRequestDto(
                follow.getFollowId(),
                follow.getFollowing().getId(),
                follow.getFollower().getId()
        );
    }
}
