package com.sparta.newsfeed.dto.ResponseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.ImageFile;
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
//    private String name;                // 작성자 닉네임
    private int cmtCnt;                 // 댓글 갯수
    private List<ImageFile> imageList;     // 이미지

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;    // 작성 시간

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;   // 수정 시간

    private List<CommentResponseDto> commentList = new ArrayList<>();

    private int like;

    public BoardResponseDto(Long id, String username, String contents, List<ImageFile> imageList,
                            LocalDateTime createdAt, LocalDateTime modifiedAt, int cmtCnt, int like) {
        this.id = id;
        this.username = username;
        this.contents = contents;
        this.imageList = imageList;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.cmtCnt = cmtCnt;
        this.like = like;
    }

    // 게시글 생성
    public BoardResponseDto(Board board, List<String> imageFileList) {
        this.id = board.getId();
        this.username = board.getUser().getUsername();
        this.contents = board.getContents();
    //    this.name = board.getUser().getName();
        this.cmtCnt = board.getCommentList().size();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    //    this.like = board.getBoardLike();
        this.imageList = board.getImageFileList();
    }

    public BoardResponseDto(Board board) {  // 게시글 전체 조회
        this.id = board.getId();
        this.contents = board.getContents();
        this.cmtCnt = board.getCommentList().size();
        this.username = board.getUser().getUsername();
    //    this.name = board.getUser().getName();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }

    public static BoardResponseDto createBoardDto(Board board) {
        return new BoardResponseDto(
                board.getId(),
                board.getUser().getUsername(),
                board.getContents(),
                board.getImageFileList(),
                board.getCreatedAt(),
                board.getModifiedAt(),
                board.getCommentList().size(),
                board.getBoardLike().size()
        );
    }
}
