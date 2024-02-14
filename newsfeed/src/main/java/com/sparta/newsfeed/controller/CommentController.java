package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.RequestDto.CommentRequestDto;
import com.sparta.newsfeed.dto.ResponseDto.CommentResponseDto;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.service.CommentService;
import com.sparta.newsfeed.util.GlobalResponse.GlobalResponseDto;
import com.sparta.newsfeed.util.GlobalResponse.ResponseUtil;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// 기능 : 댓글 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class CommentController {

    private final CommentService commentService;

    // 댓글 전체 불러오기
    @GetMapping("/{boardId}/comments/all")
    public ResponseEntity<?> getAllComment(@PathVariable Long boardId,
                                           @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseUtil.response(commentService.getAllComment(boardId, pageable));
    }

    // 댓글 작성
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long boardId,
                                                            @RequestBody CommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtil.response(commentService.createComment(boardId, requestDto, userDetails.getUser()));
    }

    // 대댓글 작성
    @PostMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> createReply(@PathVariable Long boardId,
                                                          @PathVariable Long commentId,
                                                          @RequestBody CommentRequestDto requestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtil.response(commentService.createReply(boardId, commentId, requestDto, userDetails.getUser()));
    }

    // 댓글, 대댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
                                                            @RequestBody CommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtil.response(commentService.updateComment(commentId, requestDto, userDetails.getUser()));
    }

    // 댓글, 대댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<GlobalResponseDto> deleteComment(@PathVariable Long commentId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseUtil.response(StatusCode.DELETE_OK);
    }

}
