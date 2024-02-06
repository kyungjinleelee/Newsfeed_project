package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.CommentRequestDto;
import com.sparta.newsfeed.dto.CommentResponseDto;
import com.sparta.newsfeed.entity.Board;
import com.sparta.newsfeed.entity.Comment;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.entity.UserRoleEnum;
import com.sparta.newsfeed.repository.BoardRepository;
import com.sparta.newsfeed.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private CommentRepository commentRepository;
    private BoardRepository boardRepository;

    @Transactional
    // 댓글 쓰기
    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, User user) {
        // 해당 게시글이 DB에 있는지 확인
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            throw new IllegalArgumentException("해당 글은 존재하지 않습니다.");
        }

        // RequestDto -> Entity
        Comment comment = new Comment(requestDto, board.get(), user);

        // DB 저장
        Comment saveComment = commentRepository.save(comment);

        // Entity -> ResponseDto
        CommentResponseDto commentResponseDto = new CommentResponseDto(saveComment);

        return commentResponseDto;
    }

    @Transactional
    // 댓글 수정
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {
        // 선택한 댓글이 DB에 있는지 확인
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new IllegalArgumentException("해당 댓글은 존재하지 않습니다.");
        }

        // 댓글의 작성자와 수정하려는 사용자의 정보가 일치하는지 확인 (수정하려는 사용자가 관리자라면 수정 가능)
        Optional<Comment> found = commentRepository.findByIdAndUser(id, user);
        if (found.isEmpty() && user.getRole() == UserRoleEnum.USER) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        // 관리자이거나, 댓글의 작성자와 삭제하려는 사용자의 정보가 일치한다면, 댓글 수정
        comment.get().update(requestDto, user);
        commentRepository.flush();  // requestDto 에 modifiedAt 업데이트 해주기 위해 saveAndFlush 사용

        return new CommentResponseDto(comment.get());
    }

    // 댓글 삭제
    @Transactional
    public Long deleteComment(Long id, User user) {
        // 선택한 댓글이 DB에 있는지 확인
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new IllegalArgumentException("해당 댓글은 존재하지 않습니다.");
        }

        // 댓글의 작성자와 삭제하려는 사용자의 정보가 일치하는지 확인 (삭제하려는 사용자가 관리자라면 삭제 가능)
        Optional<Comment> found = commentRepository.findByIdAndUser(id, user);
        if (found.isEmpty() && user.getRole() == UserRoleEnum.USER) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        // 관리자이거나, 댓글의 작성자와 삭제하려는 사용자의 정보가 일치한다면, 댓글 삭제
        commentRepository.deleteById(id);

        return id;
    }
}
