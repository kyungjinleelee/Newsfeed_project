package com.sparta.newsfeed.dto.ResponseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.newsfeed.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 기능 : 게시글 관련 반환값을 담을 Dto
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {
    private Long id;                    // 고유 id
    private String username;            // 작성자 아이디
    private String contents;            // 게시글 내용
    private String name;                // 작성자 닉네임
    private int cmtCnt;                 // 댓글 갯수
    private List<String> imageList;     // 이미지

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;    // 작성 시간

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;   // 수정 시간

    private List<CommentResponseDto> commentList = new ArrayList<>();

    // 게시글 생성
    public BoardResponseDto(Board board, List<String> imageFileList) {
        this.id = board.getId();
        this.username = board.getUser().getUsername();
        this.contents = board.getContents();
        this.name = board.getUser().getName();
        this.cmtCnt = board.getCommentList().size();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.imageList = imageFileList;
    }

    public BoardResponseDto(Board board) {  // 게시글 전체 조회
        this.id = board.getId();
        this.contents = board.getContents();
        this.cmtCnt = board.getCommentList().size();
        this.username = board.getUser().getUsername();  // 이건 임시로
        this.name = board.getUser().getName();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }
}
