package com.sparta.newsfeed.service;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.Multimedia;
import com.sparta.newsfeed.domain.User;
import com.sparta.newsfeed.domain.UserRoleEnum;
import com.sparta.newsfeed.domainModel.BoardCommand;
import com.sparta.newsfeed.domainModel.BoardQuery;
import com.sparta.newsfeed.dto.RequestDto.BoardRequestDto;
import com.sparta.newsfeed.dto.ResponseDto.BoardResponseDto;
import com.sparta.newsfeed.repository.BoardRepository;
import com.sparta.newsfeed.util.GlobalResponse.CustomException;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// 기능 : 보드 CRUD 서비스
@Slf4j(topic = "보드 서비스")
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardQuery boardQuery;
    private final BoardCommand boardCommand;
    private final BoardRepository boardRepository;


    // 글 전체 보기
    public Page<BoardResponseDto> getBoards(Pageable pageable) {
        // DB 조회
        Page<Board> boards = boardRepository.findAll(pageable);
        return new PageImpl<>(
                boards.getContent().stream()
                        .map(BoardResponseDto::createBoardDto)
                        .collect(Collectors.toList()),
                pageable,
                boards.getTotalElements()
        );
    }

    // 글 쓰기
    @Transactional
    public BoardResponseDto createBoardContents(BoardRequestDto requestDto, User user) {
        // RequestDto -> Entity
        Board board = new Board(requestDto, user);

        // DB 저장
        boardCommand.saveBoard(board);

        // Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);

        return boardResponseDto;
    }


    // 게시글 상세 조회
    public List<BoardResponseDto> getOneBoard(Long id) {
        Board board = boardQuery.findBoardById(id);
        List<BoardResponseDto> result = new ArrayList<>();

        List<String> imageFileList = new ArrayList<>();
        for (Multimedia multimedia : board.getMultimediaList()) {
            imageFileList.add(multimedia.getFileUrl());
        }

        result.add(new BoardResponseDto(board, imageFileList));
        return result;
    }

    // 글 수정
    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, User user) {
        // 해당 글이 DB에 있는지 확인
        Board board = boardQuery.findBoardById(id);

        // 선택한 게시글의 작성자와 토큰에서 가져온 사용자 정보가 일치하는지 확인 (수정하려는 유저가 관리자라면 글 수정 가능)
        Optional<Board> found = boardRepository.findByIdAndUser(id, user);
        if (found.isEmpty() && user.getRole() == UserRoleEnum.USER) {
            throw new CustomException(StatusCode.BAD_AUTHORITY);
        }

        // 게시글 ID와 사용자 정보 일치한다면, board 내용 수정
        board.update(requestDto, user);       // Board.java에 update 메서드

        return new BoardResponseDto(board);
    }

    // 글 삭제
    @Transactional
    public void deleteBoard(Long id, User user) {

        // 해당 글이 DB에 있는지 확인
        Board board = boardQuery.findBoardById(id);

        // 선택한 게시글의 작성자와 토큰에서 가져온 사용자 정보가 일치하는지 확인 (관리자면 삭제 가능)
        Optional<Board> found = boardRepository.findByIdAndUser(id, user);
        if (found.isEmpty() && user.getRole() == UserRoleEnum.USER) {
            throw new CustomException(StatusCode.BAD_AUTHORITY);
        }

        // 게시글 id와 사용자 정보 일치하면, 게시글 삭제
        boardCommand.deleteBoard(id);
    }

    // 글 검색 (내용, 키워드)
    @Transactional(readOnly = true)
    public Page<BoardResponseDto> findByOption(String keyword, Pageable pageable) {
        Page<Board> searchResults = boardRepository.findByOption(keyword, pageable);
        if (searchResults.isEmpty()) {
            throw new CustomException(StatusCode.BOARD_NOT_FOUND);
        }
        return new PageImpl<>(
                searchResults.getContent().stream()
                        .map(BoardResponseDto::createBoardDto)
                        .collect(Collectors.toList()),
                pageable,
                searchResults.getTotalElements()
        );
    }


    // 글 검색 (username으로 검색)
    public Page<BoardResponseDto> findByUser(String username, Pageable pageable) {
        Page<Board> searchResults = boardRepository.findByUser(username, pageable);
        if (searchResults.isEmpty()) {
            throw new CustomException(StatusCode.BOARD_NOT_FOUND);
        }
        return new PageImpl<>(
                searchResults.getContent().stream()
                        .map(BoardResponseDto::createBoardDto)
                        .collect(Collectors.toList()),
                pageable,
                searchResults.getTotalElements()
        );
    }

    // 글 검색 (닉네임으로 검색)
    public Page<BoardResponseDto> findByName(String name, Pageable pageable) {
        Page<Board> searchResults = boardRepository.findByName(name, pageable);
        if (searchResults.isEmpty()) {
            throw new CustomException(StatusCode.BOARD_NOT_FOUND);
        }
        return new PageImpl<>(
                searchResults.getContent().stream()
                        .map(BoardResponseDto::createBoardDto)
                        .collect(Collectors.toList()),
                pageable,
                searchResults.getTotalElements()
        );
    }
}
