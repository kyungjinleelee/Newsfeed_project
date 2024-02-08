package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.BoardDetailDto;
import com.sparta.newsfeed.dto.BoardRequestDto;
import com.sparta.newsfeed.dto.BoardResponseDto;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.service.BoardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;
    public BoardController(BoardService boardService) { this.boardService = boardService; }

    // 글 보기
    @GetMapping("/boards")
    public List<BoardResponseDto> getBoards() { return boardService.getBoards(); }

    // 글 쓰기
    @PostMapping("/boards")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.createBoard(requestDto, userDetails.getUser());
    }

    // 글 수정
    @PutMapping("/boards/{id}")
    public Long updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.updateBoard(id, requestDto, userDetails.getUser());
    }

    // 글 삭제
    @DeleteMapping("/boards/{id}")
    public Long deleteBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deleteBoard(id, userDetails.getUser());
    }

    // 글 상세보기
    @GetMapping("/boards/{id}")
    public BoardDetailDto show(@PathVariable Long id){
        return boardService.show(id);
    }

}
