package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.CommentRequestDto;
import com.sparta.newsfeed.dto.CommentResponseDto;
import com.sparta.newsfeed.entity.Board;
import com.sparta.newsfeed.entity.Comment;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.repository.BoardRepository;
import com.sparta.newsfeed.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Optional<Board> found = boardRepository.findById(id);
        if (found.isEmpty()) {
            throw new IllegalArgumentException("해당 글은 존재하지 않습니다.");
        }

        // RequestDto -> Entity
        Comment comment = new Comment(requestDto, user);

        // DB 저장
        Comment saveComment = commentRepository.save(comment);

        // Entity -> ResponseDto
        CommentResponseDto commentResponseDto = new CommentResponseDto(saveComment);

        return commentResponseDto;
    }
}
