package com.sparta.newsfeed.dto.RequestDto;

import com.sparta.newsfeed.domain.Like;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

// 기능 : 좋아요 요청 Dto
@Getter
@AllArgsConstructor
@ToString
public class LikeRequestDto {

    private Long id;
    private String username;
    private Long boardId;

    // 좋아요 하기
    public static LikeRequestDto like(Like like) {
        return new LikeRequestDto(
                like.getId(),
                like.getUser().getUsername(),
                like.getBoard().getId()
        );
    }

    // 좋아요 취소
    public static LikeRequestDto cancel(Like like) {
        return new LikeRequestDto(
                like.getId(),
                like.getUser().getUsername(),
                like.getBoard().getId()
        );
    }
}
