package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.aop.annotation.ExeTimer;
import com.sparta.newsfeed.dto.ResponseDto.FollowingBoardDto;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.service.FollowService;
import com.sparta.newsfeed.util.GlobalResponse.GlobalResponseDto;
import com.sparta.newsfeed.util.GlobalResponse.ResponseUtil;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FollowController {

    private final FollowService followService;

    // 팔로우 등록 (삭제)
    @ExeTimer
    @PostMapping("/follow/{followingId}")   // 팔로우 하려는 유저의 id
    public ResponseEntity<GlobalResponseDto> create(@PathVariable Long followingId, @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Long followerId = userDetails.getUser().getId();
        followService.create(followingId, followerId);

        return ResponseUtil.response(StatusCode.FOLLOW_OK);
    }

    // 팔로우하는 유저 게시물 보기
    @GetMapping("/follow/board")
    public ResponseEntity<List<FollowingBoardDto>> show(@AuthenticationPrincipal final UserDetailsImpl userDetails) {
       Long followerId = userDetails.getUser().getId();
       List<FollowingBoardDto> boards = followService.findAll(followerId);
       return ResponseUtil.response(boards);
    }
}
