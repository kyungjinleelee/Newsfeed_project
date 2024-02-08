package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.BoardDetailDto;
import com.sparta.newsfeed.dto.BoardRequestDto;
import com.sparta.newsfeed.dto.BoardResponseDto;
import com.sparta.newsfeed.entity.Board;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.entity.UserRoleEnum;
import com.sparta.newsfeed.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private BoardRepository boardRepository;


    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 전체보기
    public List<BoardResponseDto> getBoards() {
        // DB 조회
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    @Transactional
    // 글 쓰기
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
        // RequestDto -> Entity
        Board board = new Board(requestDto, user);

        // DB 저장
        Board saveBoard = boardRepository.save(board);

        // Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(saveBoard);

        return boardResponseDto;

    }

    // 글 수정
    @Transactional
    public Long updateBoard(Long id, BoardRequestDto requestDto, User user) {
        // 해당 글이 DB에 있는지 확인
        Board board = findBoard(id);

        // 선택한 게시글의 작성자와 토큰에서 가져온 사용자 정보가 일치하는지 확인 (수정하려는 유저가 관리자라면 글 수정 가능)
        Optional<Board> found = boardRepository.findByIdAndUser(id, user);
        if (found.isEmpty() && user.getRole() == UserRoleEnum.USER) {
            throw new IllegalArgumentException("사용자가 올바르지 않습니다.");
        }

        // 게시글 ID와 사용자 정보 일치한다면, board 내용 수정
        board.update(requestDto, user);       // Board.java에 update 메서드

        return id;
    }

    // 글 삭제
    @Transactional
    public Long deleteBoard(Long id, User user) {

        // 해당 글이 DB에 있는지 확인
        Optional<Board> found = boardRepository.findById(id);
        if (found.isEmpty()) {
            throw new IllegalArgumentException("선택한 글은 존재하지 않습니다.");
        }

        // 선택한 게시글의 작성자와 토큰에서 가져온 사용자 정보가 일치하는지 확인 (관리자면 삭제 가능)
        Optional<Board> board = boardRepository.findByIdAndUser(id, user);
        if (board.isEmpty() && user.getRole() == UserRoleEnum.USER) {
            throw new IllegalArgumentException("권한이 잘못되었습니다.");
        }

        // 게시글 id와 사용자 정보 일치하면, 게시글 삭제
        boardRepository.deleteById(id);

        return id;
    }

    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->   // board가 null이라면 exception 반환
                new IllegalArgumentException("선택한 글은 존재하지 않습니다.")
        );
    }

    // 게시글 상세
    public BoardDetailDto show(Long id) {
        return boardRepository.findById(id)
                .map(BoardDetailDto::createBoardDto)
                .orElse(null);
    }
}
