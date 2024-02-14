package com.sparta.newsfeed.domainModel;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.Comment;
import com.sparta.newsfeed.repository.BoardRepository;
import com.sparta.newsfeed.repository.CommentRepository;
import com.sparta.newsfeed.util.GlobalResponse.CustomException;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 기능 : 보드 도메인 관련 DB Read 관리
@Service
@RequiredArgsConstructor
public class BoardQuery {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    //////////////TODO 보드 관련
    // 보드 ID로 보드 찾아오기
    public Board findBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(StatusCode.BOARD_NOT_FOUND)
        );
        return board;
    }

    //////////////TODO 댓글 관련
    // 댓글 ID로 댓글 찾아오기
    public Comment findCommentById(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(StatusCode.COMMENT_NOT_FOUND)
        );
        return comment;
    }
}
