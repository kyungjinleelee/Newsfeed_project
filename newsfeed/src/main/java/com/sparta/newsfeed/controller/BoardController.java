package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.RequestDto.BoardRequestDto;
import com.sparta.newsfeed.dto.ResponseDto.BoardResponseDto;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.service.BoardService;
import com.sparta.newsfeed.util.GlobalResponse.GlobalResponseDto;
import com.sparta.newsfeed.util.GlobalResponse.ResponseUtil;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// 기능 : 포스트 관련 CRUD 컨트롤러
@RestController
@RequestMapping("/api")
@Slf4j(topic = "보드 컨트롤러")
public class BoardController {

    private final BoardService boardService;
    public BoardController(BoardService boardService) { this.boardService = boardService; }

    // 글 보기
    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponseDto>> getBoards() {
        return ResponseUtil.response(boardService.getBoards());
    }

    // 글 쓰기
    @PostMapping(value = "/boards", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BoardResponseDto> createBoard(@RequestPart(value = "data") BoardRequestDto requestDto,
                                                        @RequestPart(value = "file", required = false) List<MultipartFile> multipartFilelist,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("글쓰기");
        return ResponseUtil.response(boardService.createBoard(requestDto, multipartFilelist, userDetails.getUser()));
    }

    // 글 상세 보기
    @GetMapping("/boards/{id}")
    public ResponseEntity<List<BoardResponseDto>> getOneBoard(@PathVariable Long id){
        return ResponseUtil.response(boardService.getOneBoard(id));
    }

    // 글 수정
    @PutMapping("/boards/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long id,
                                                        @RequestPart(value = "data") BoardRequestDto requestDto,
                                                        @RequestPart(value = "file", required = false) List<MultipartFile> multipartFilelist,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtil.response(boardService.updateBoard(id, requestDto, multipartFilelist, userDetails.getUser()));
    }

    // 글 삭제
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<GlobalResponseDto> deleteBoard(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(id, userDetails.getUser());
        return ResponseUtil.response(StatusCode.DELETE_OK);
    }

}
