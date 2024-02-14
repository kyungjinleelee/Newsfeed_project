package com.sparta.newsfeed.service;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.Comment;
import com.sparta.newsfeed.domain.User;
import com.sparta.newsfeed.domainModel.BoardCommand;
import com.sparta.newsfeed.domainModel.BoardQuery;
import com.sparta.newsfeed.dto.RequestDto.CommentRequestDto;
import com.sparta.newsfeed.dto.ResponseDto.CommentResponseDto;
import com.sparta.newsfeed.dto.ResponseDto.CommentResponseListDto;
import com.sparta.newsfeed.repository.CommentRepository;
import com.sparta.newsfeed.util.GlobalResponse.CustomException;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// 기능 : 댓글 서비스
@Service
@Slf4j
public class CommentService {

    private final BoardQuery boardQuery;
    private final BoardCommand boardCommand;
    private CommentRepository commentRepository;

    public CommentService(BoardQuery boardQuery, BoardCommand boardCommand, CommentRepository commentRepository) {
        this.boardQuery = boardQuery;
        this.boardCommand = boardCommand;
        this.commentRepository = commentRepository;
    }


    // 댓글 전체 불러오기
    public CommentResponseListDto getAllComment(Long id, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllByBoard_IdOrderByCreatedAtDesc(pageable, id);

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }
        int totalPage = comments.getTotalPages();
        return new CommentResponseListDto(totalPage, commentResponseDtoList);
    }

    // 댓글 작성
    public CommentResponseDto createComment(Long boardId, CommentRequestDto requestDto, User user) {
        // 매개변수로 받아온 포스트 Id를 활용해서 Board 객체 저장
        Board board = boardQuery.findBoardById(boardId);

        // RequestDto -> Entity
        Comment comment = new Comment(requestDto, board, user);

        // DB 저장
        boardCommand.saveComment(comment);

        // Entity -> ResponseDto 에 담아 반환
        return new CommentResponseDto(comment);
    }

    // 대댓글 작성
    public CommentResponseDto createReply(Long boardId, Long commentId, CommentRequestDto requestDto, User user) {
        Board board = boardQuery.findBoardById(boardId);

        Comment comment = boardQuery.findCommentById(commentId);

        Comment reply = new Comment(requestDto, user, board, comment);
        boardCommand.saveComment(reply);
        return new CommentResponseDto(reply);
    }

    // 댓글, 대댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        // 매개변수로 받아온 코멘트 Id를 활용해서 Comment 객체 저장
        Comment comment = boardQuery.findCommentById(commentId);

        // 댓글의 작성자와 수정하려는 사용자의 정보가 일치하는지 확인 (수정하려는 사용자가 관리자라면 수정 가능)
        if (!user.getUsername().equals(comment.getUsername())) {
            throw new CustomException(StatusCode.NO_AUTH_USER);
        }

        // 관리자이거나, 댓글의 작성자와 삭제하려는 사용자의 정보가 일치한다면, 댓글 수정
        comment.update(requestDto, user);
     //   commentRepository.flush();  // requestDto 에 modifiedAt 업데이트 해주기 위해 saveAndFlush 사용

        return new CommentResponseDto(comment);
    }

    // 댓글, 대댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, User user) {
        // 매개변수로 받아온 코멘트 Id를 활용해서 Comment 객체 저장
        Comment comment = boardQuery.findCommentById(commentId);

        // 매개변수로 받아온 닉네임과 코멘트의 작성자가 동일하지 않다면 예외 처리
        if (!user.getUsername().equals(comment.getUsername())) {
            throw new CustomException(StatusCode.NO_AUTH_USER);
        }

        // 관리자이거나, 댓글의 작성자와 삭제하려는 사용자의 정보가 일치한다면, 댓글 삭제
        boardCommand.deleteComment(comment);
    }
}
