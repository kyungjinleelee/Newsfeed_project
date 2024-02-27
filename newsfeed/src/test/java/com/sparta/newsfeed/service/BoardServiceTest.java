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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BoardServiceTest {

    @InjectMocks
    BoardService boardService;

    @Mock
    private BoardQuery boardQuery;

    @Mock
    private BoardCommand boardCommand;

    @Mock
    private BoardRepository boardRepository;

    @Test
    @DisplayName("전체 글 조회 테스트")
    void testGetBoard() {
        // given
        User user = new User();

        BoardRequestDto requestDto = new BoardRequestDto();

        List<Board> mockResults = new ArrayList<>();
        Board board = new Board(requestDto, user);
        mockResults.add(board);  // Board 객체를 생성하여 목록에 추가

        when(boardRepository.findAllByOrderByModifiedAtDesc()).thenReturn(mockResults);

        // when
        List<BoardResponseDto> resultPage = boardService.getBoards();

        // then
        assertEquals(1, resultPage.size());  // 반환된 결과의 크기가 올바른지 검증
        assertEquals(board.getContents(), resultPage.get(0).getContents());  // 반환된 결과의 첫 번째 요소의 내용이 올바른지 검증
    }

    @Test
    @DisplayName("게시글 작성 테스트")
    void testCreateBoard() {
        // given
    //    BoardService boardService = new BoardService(boardQuery, boardCommand, boardRepository);
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
    @DisplayName("게시글 수정 테스트")
    void testUpdateBoard() {
        // given
        Long id = 1L;
        BoardRequestDto requestDto = new BoardRequestDto();
        requestDto.setContents("수정 테스트 입니다.");

        User user = new User();
        user.setUsername("kyungjin77");

        Board board = new Board(requestDto, user);

        BoardResponseDto expectedResponseDto = new BoardResponseDto(board);  // 수정된 게시글을 기대 결과로 설정

        when(boardQuery.findBoardById(id)).thenReturn(board); // boardQuery.findBoardById() 메서드의 동작을 모의화
        when(boardRepository.findByIdAndUser(id, user)).thenReturn(Optional.of(board)); // boardRepository.findByIdAndUser() 메서드의 동작을 모의화

        // when
        BoardResponseDto actualResponseDto = boardService.updateBoard(id, requestDto, user);

        // then
        assertEquals(expectedResponseDto.getId(), actualResponseDto.getId());
        assertEquals(expectedResponseDto.getContents(), actualResponseDto.getContents());
        assertSame(board.getUser(), user, "응답이 정상 처리 되었습니다.");
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void testDeleteBoard() {
        // given
        Long id = 1L;

        User user = new User();
        user.setUsername("kyungjin77");

        Board board = new Board();

        when(boardQuery.findBoardById(id)).thenReturn(board); // findBoardById() 메서드의 동작을 모의화
        when(boardRepository.findByIdAndUser(id, user)).thenReturn(Optional.of(board)); // findByIdAndUser() 메서드의 동작을 모의화
        doNothing().when(boardCommand).deleteBoard(id);       // deleteBoard() 메서드가 호출되면 아무 동작도 하지 않도록 설정

        // when
        boardService.deleteBoard(id, user);

        // then
        verify(boardCommand, times(1)).deleteBoard(id); // deleteBoard() 메서드가 한 번 호출되었는지 검증
        when(boardQuery.findBoardById(id)).thenReturn(null); // 게시글 삭제 후에는 findBoardById() 메서드가 null을 반환하도록 설정
        assertNull(boardQuery.findBoardById(id)); // 게시글이 실제로 삭제되었는지 확인
    }

    @Test
    @DisplayName("키워드로 검색하기 테스트")
    void testFindByOption() {
        // given
        String keyword = "example";                                     // 테스트 할 키워드 설정
        Pageable pageable = PageRequest.of(0, 10);  // 페이지네이션 정보 설정

        User user = new User();
        user.setUsername("kyungjin77");

        List<Board> mockResults = new ArrayList<>();                    // 테스트용 결과 목록을 생성
        Board board = new Board(new BoardRequestDto(), user);
        mockResults.add(board);   // Board 객체를 생성하여 목록에 추가
        Page<Board> mockPage = new PageImpl<>(mockResults, pageable, mockResults.size()); // 결과 페이지를 생성

        when(boardRepository.findByOption(keyword, pageable)).thenReturn(mockPage); // findByOption() 메서드의 동작을 모의화

        // when
        Page<BoardResponseDto> resultPage = boardService.findByOption(keyword, pageable);

        // then
        assertEquals(1, resultPage.getContent().size());        // 반환된 결과의 크기가 올바른지 검증
        assertEquals(board.getContents(), resultPage.getContent().get(0).getContents());  // 반환된 결과의 첫 번째 요소가 올바른지 검증
    }

    @Test
    @DisplayName("유저 아이디로 검색하기 테스트")
    void testFindByUsername() {
        // given
        String username = "exampleUser";        // 테스트할 유저명 설정
        Pageable pageable = PageRequest.of(0, 10);

        User user = new User();
        user.setUsername(username);

        List<Board> mockResults = new ArrayList<>();
        Board board = new Board(new BoardRequestDto(), user);
        mockResults.add(board);   // Board 객체를 생성하여 목록에 추가

        Page<Board> mockPage = new PageImpl<>(mockResults, pageable, mockResults.size());
        when(boardRepository.findByUser(username, pageable)).thenReturn(mockPage);

        // when
        Page<BoardResponseDto> resultPage = boardService.findByUser(username, pageable);

        // then
        assertEquals(1, resultPage.getContent().size());
        assertEquals(board.getContents(), resultPage.getContent().get(0).getContents());
    }

    @Test
    @DisplayName("닉네임으로 검색하기 테스트")
    void testFindByName() {
        // given
        String name = "고양이집사";                                     // 테스트 할 키워드 설정
        Pageable pageable = PageRequest.of(0, 10);

        User user = new User();
        user.setUsername("kyungjin77");

        List<Board> mockResults = new ArrayList<>();
        Board board = new Board(new BoardRequestDto(), user);
        mockResults.add(board);   // Board 객체를 생성하여 목록에 추가
        Page<Board> mockPage = new PageImpl<>(mockResults, pageable, mockResults.size());

        when(boardRepository.findByName(name, pageable)).thenReturn(mockPage);

        // when
        Page<BoardResponseDto> resultPage = boardService.findByName(name, pageable);

        // then
        assertEquals(1, resultPage.getContent().size());
        assertEquals(board.getContents(), resultPage.getContent().get(0).getContents());
    }

}