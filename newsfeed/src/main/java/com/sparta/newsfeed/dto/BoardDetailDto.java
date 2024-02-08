package com.sparta.newsfeed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.newsfeed.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class BoardDetailDto {

    private Long id;
    private String contents;

    @JsonProperty("user_id")
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static BoardDetailDto createBoardDto(Board board) {
        return new BoardDetailDto(
                board.getId(),
                board.getContents(),
                board.getUser().getUsername(),
                board.getCreatedAt(),
                board.getModifiedAt()
        );
    }
}
