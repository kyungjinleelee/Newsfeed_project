package com.sparta.newsfeed.domainModel;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.Comment;
import com.sparta.newsfeed.repository.BoardRepository;
import com.sparta.newsfeed.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 기능 : 보드 도메인 관련 DB CUD 관리
@Service
@RequiredArgsConstructor
public class BoardCommand {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    //////////////TODO 보드 관련
    // 글 저장하기
    public Board saveBoard(Board board) {
        boardRepository.save(board);
        return board;
    }

    // 글 삭제하기
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

    //////////////TODO 댓글 관련
    // 댓글 저장
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
