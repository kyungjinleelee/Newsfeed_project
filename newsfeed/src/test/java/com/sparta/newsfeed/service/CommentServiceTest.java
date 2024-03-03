package com.sparta.newsfeed.service;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.Comment;
import com.sparta.newsfeed.domain.User;
import com.sparta.newsfeed.domain.UserRoleEnum;
import com.sparta.newsfeed.domainModel.BoardCommand;
import com.sparta.newsfeed.domainModel.BoardQuery;
import com.sparta.newsfeed.dto.RequestDto.CommentRequestDto;
import com.sparta.newsfeed.dto.ResponseDto.CommentResponseDto;
import com.sparta.newsfeed.dto.ResponseDto.CommentResponseListDto;
import com.sparta.newsfeed.repository.CommentRepository;
import com.sparta.newsfeed.util.GlobalResponse.CustomException;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CommentServiceTest {

    @InjectMocks
    CommentService commentService;
    @Mock
    private BoardQuery boardQuery;
    @Mock
    private BoardCommand boardCommand;
    @Mock
    private CommentRepository commentRepository;


    @Test
    @DisplayName("댓글 전체 보기 성공 테스트")
    void testGetAllComment() {
        // given
        User user = new User();
        Board board = new Board();
        board.setId(1L);
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setBoard(board);

        Pageable pageable = PageRequest.of(0, 10);
        List<Comment> mockResults = new ArrayList<>();
        mockResults.add(comment);  // Comment 객체를 생성하여 목록에 추가

        // when
        when(commentRepository.findAllByBoard_IdOrderByCreatedAtDesc(pageable, 1L)).thenReturn(new PageImpl<>(mockResults));
        CommentResponseListDto result = commentService.getAllComment(1L, pageable);

        // then
        assertEquals(1, result.getCommentResponseDtoList().size());  // 반환된 결과의 크기가 올바른지 검증
        assertEquals(comment.getComment(), result.getCommentResponseDtoList().get(0).getComment());  // 반환된 결과의 첫 번째 요소의 내용이 올바른지 검증
    }

    @Test
    @DisplayName("댓글 작성 성공 테스트")
    void testCreateComment() {
        // given
        User user = new User();
        Board board = new Board();
        board.setId(1L);

        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setComment("고양이가 너무 귀엽네요!");

        Comment comment = new Comment(requestDto, board, user);

        // when
        when(boardQuery.findBoardById(anyLong())).thenReturn(board);
        doNothing().when(boardCommand).saveComment(any(Comment.class));

        CommentResponseDto result = commentService.createComment(1L, requestDto, user);

        // then
        assertEquals(comment.getComment(), result.getComment());  // 반환된 결과의 내용이 올바른지 검증
    }

    @Test
    @DisplayName("대댓글 작성 성공 테스트")
    void testCreateReply() {
        // given
        User user = new User();
        Board board = new Board();
        board.setId(1L);

        Comment parentComment = new Comment();
        parentComment.setId(1L);

        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setComment("칭찬 감사합니다!");

        Comment reply = new Comment(requestDto, user, board, parentComment);

        // when
        when(boardQuery.findBoardById(anyLong())).thenReturn(board);
        when(boardQuery.findCommentById(anyLong())).thenReturn(parentComment);
        doNothing().when(boardCommand).saveComment(any(Comment.class));

        CommentResponseDto result = commentService.createReply(1L, 1L, requestDto, user);

        // then
        assertEquals(reply.getComment(), result.getComment());
    }

    @Nested
    @DisplayName("댓글, 대댓글 수정 테스트")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Update {
        @Test
        @Order(1)
        @DisplayName("댓글, 대댓글 수정 성공 테스트")
        void testUpdateComment() {
            // given
            User user = new User();
            user.setUsername("catlover97");

            Comment comment = new Comment();
            comment.setUsername("catlover97");  // 사용자가 댓글의 작성자인지 확인

            CommentRequestDto requestDto = new CommentRequestDto();
            requestDto.setComment("강아지가 너무 귀엽네요!");

            // when
            when(boardQuery.findCommentById(anyLong())).thenReturn(comment);
            CommentResponseDto result = commentService.updateComment(1L, requestDto, user);

            // then
            assertEquals(requestDto.getComment(), result.getComment());
        }

        @Test
        @Order(2)
        @DisplayName("댓글, 대댓글 수정 실패 테스트 - 권한 없음")
        void testUpdateCommentFail_UnauthorizedUser() {
            // given
            Long commentId = 1L;
            CommentRequestDto requestDto = new CommentRequestDto();
            requestDto.setComment("강아지가 너무 귀엽네요!");

            User user = new User();
            user.setUsername("unauthorizedUser");

            User commentOwner = new User();
            commentOwner.setUsername("kyungjin77");

            Board board = new Board(); // 댓글이 속한 게시글
            Comment comment = new Comment(requestDto, board, commentOwner);

            // when
            when(boardQuery.findCommentById(commentId)).thenReturn(comment);
            CustomException exception = assertThrows(CustomException.class, () -> {
                commentService.updateComment(commentId, requestDto, user);
            });

            // then
            assertEquals(StatusCode.NO_AUTH_USER, exception.getStatusCode());
        }
    }

    @Nested
    @DisplayName("댓글, 대댓글 삭제 테스트")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Delete {
        @Test
        @Order(1)
        @DisplayName("댓글, 대댓글 삭제 성공 테스트")
        void testDeleteComment() {
            // given
            Long commentId = 1L;
            User user = new User();
            Board board = new Board();
            Comment comment = new Comment(new CommentRequestDto(), board, user);

            // when
            when(boardQuery.findCommentById(commentId)).thenReturn(comment);
            when(commentRepository.findByIdAndUser(commentId, user)).thenReturn(Optional.of(comment));

            // execute
            commentService.deleteComment(commentId, user);

            // then
            verify(boardCommand, times(1)).deleteComment(comment);  // deleteComment 메서드가 성공적으로 실행되었을 때 boardCommand.deleteComment(comment)가 한 번 호출되는 것을 확인
        }

        @Test
        @Order(2)
        @DisplayName("댓글, 대댓글 삭제 실패 테스트 - 권한 없음")
        void testDeleteCommentFail_UnauthorizedUser() {
            // given
            Long commentId = 1L;

            User user = new User();
            user.setUsername("unauthorizedUser");
            user.setRole(UserRoleEnum.USER);    // 권한이 없는 사용자로 설정

            User commentOwner = new User();
            commentOwner.setUsername("kyungjin77");

            Board board = new Board();
            Comment comment = new Comment(new CommentRequestDto(), board, commentOwner);

            // when
            when(boardQuery.findCommentById(commentId)).thenReturn(comment);
            when(commentRepository.findByIdAndUser(commentId, user)).thenReturn(Optional.empty());

            CustomException exception = assertThrows(CustomException.class, () -> {
                commentService.deleteComment(commentId, user);
            });
            // then
            assertEquals(StatusCode.BAD_AUTHORITY, exception.getStatusCode());
        }
    }
}