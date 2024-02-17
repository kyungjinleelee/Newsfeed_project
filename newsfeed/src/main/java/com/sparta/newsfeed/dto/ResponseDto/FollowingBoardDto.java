package com.sparta.newsfeed.dto.ResponseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.newsfeed.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

// 기능 : 팔로우한 유저의 글 보기 반환 Dto
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FollowingBoardDto {

    private Long boardId;

    @JsonProperty("following_Id")
    private Long followingId;   // 팔로우 당한 사람 id

    @JsonProperty("user_id")
    private Long userId;        // 글 쓴 유저 id

    private String username;    // 팔로우 당한 사람 username

    private String contents;    // 게시글 내용

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;    // 작성 시간

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;   // 수정 시간

    public static FollowingBoardDto createFollowingBoardDto(Board board) {
        return new FollowingBoardDto(
                board.getId(),
                board.getUser().getId(),
                board.getUser().getId(),
                board.getUser().getUsername(),
                board.getContents(),
                board.getCreatedAt(),
                board.getModifiedAt()
        );
    }
}
