package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.aop.annotation.ExeTimer;
import com.sparta.newsfeed.dto.RequestDto.BoardRequestDto;
import com.sparta.newsfeed.dto.ResponseDto.BoardResponseDto;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.service.BoardService;
import com.sparta.newsfeed.util.GlobalResponse.GlobalResponseDto;
import com.sparta.newsfeed.util.GlobalResponse.ResponseUtil;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    // 글 쓰기 (글)
    @PostMapping(value = "/boards/contents")
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtil.response(boardService.createBoardContents(requestDto, userDetails.getUser()));
    }

    // 글 쓰기 (이미지)
    // @PostMapping(value = "/boards/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
// public ResponseEntity<BoardResponseDto> createBoard(@RequestPart(value = "file", required = false) List<MultipartFile> multipartFilelist,
// @AuthenticationPrincipal UserDetailsImpl userDetails) {
// log.info("글(이미지) 쓰기");
// return ResponseUtil.response(boardService.createBoardImages(multipartFilelist, userDetails.getUser()));
// }

    // 글 상세 보기
    @ExeTimer
    @GetMapping("/boards/{id}")
    public ResponseEntity<List<BoardResponseDto>> getOneBoard(@PathVariable Long id){
        return ResponseUtil.response(boardService.getOneBoard(id));
    }

    // 글 수정
    @PutMapping("/boards/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long id,
                                                        @RequestBody BoardRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtil.response(boardService.updateBoard(id, requestDto, userDetails.getUser()));
    }

    // 글 수정 (이미지)
//    @PutMapping("/boards/{id}")
//    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long id,
//                                                        @RequestPart(value = "file", required = false) List<MultipartFile> multipartFilelist,
//                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return ResponseUtil.response(boardService.updateBoard(id, multipartFilelist, userDetails.getUser()));
//    }

    // 글 삭제
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<GlobalResponseDto> deleteBoard(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(id, userDetails.getUser());
        return ResponseUtil.response(StatusCode.DELETE_OK);
    }

    // 글 검색 (내용, 키워드)
    @GetMapping("/boards/search/keyword")
    public ResponseEntity<Page<BoardResponseDto>> searchKeywords(@RequestParam(name = "keyword") String keyword, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseUtil.response(boardService.findByOption(keyword, pageable));
    }

    // 글 검색 (username)
    @GetMapping("/boards/search/user")
    public ResponseEntity<Page<BoardResponseDto>> searchUser(@RequestParam("username") String username, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseUtil.response(boardService.findByUser(username, pageable));
    }

    // 글 검색 (닉네임)
    @GetMapping("/boards/search/name")
    public ResponseEntity<Page<BoardResponseDto>> searchName(@RequestParam("name") String name, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseUtil.response(boardService.findByName(name, pageable));
    }

}
