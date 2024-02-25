package com.sparta.newsfeed.service;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.User;
import com.sparta.newsfeed.domainModel.BoardCommand;
import com.sparta.newsfeed.domainModel.BoardQuery;
import com.sparta.newsfeed.dto.RequestDto.BoardRequestDto;
import com.sparta.newsfeed.dto.ResponseDto.BoardResponseDto;
import com.sparta.newsfeed.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BoardServiceTest {

//    @InjectMocks
//    BoardService boardService;

    @Mock
    private BoardQuery boardQuery;

    @Mock
    private BoardCommand boardCommand;

    @Mock
    private BoardRepository boardRepository;

    @Test
    @DisplayName("mockito")
    void 전체글조회() {
        // given



        // when

        // then
    }

    @Test
    @DisplayName("게시글 작성 테스트")
    void testCreateBoard() {
        // given
        BoardService boardService = new BoardService(boardQuery, boardCommand, boardRepository);
        BoardRequestDto requestDto = new BoardRequestDto();
        requestDto.setContents("테스트 글 입니다.");

        User user = new User();
        user.setUsername("kyungjin77");

        Board board = new Board(requestDto, user);
        BoardResponseDto expectedResponseDto = new BoardResponseDto(board);

        when(boardCommand.saveBoard(any(Board.class))).thenReturn(board);

        // when
        BoardResponseDto actualResponseDto = boardService.createBoardContents(requestDto, user);
        // then
        assertEquals(expectedResponseDto.getId(), actualResponseDto.getId());
        assertEquals(expectedResponseDto.getUsername(), actualResponseDto.getUsername());
        assertEquals(expectedResponseDto.getContents(), actualResponseDto.getContents());
    }

    @Test
    void 상세보기() {
        // given

        // when

        // then
    }

    @Test
    void 글수정하기() {
        // given

        // when

        // then
    }

    @Test
    void 글삭제하기() {
        // given

        // when

        // then
    }

    @Test
    void 키워드로검색하기() {
        // given

        // when

        // then
    }

    @Test
    void 유저로검색하기() {
        // given

        // when

        // then
    }

    @Test
    void 닉네임으로검색하기() {
        // given

        // when

        // then
    }
}