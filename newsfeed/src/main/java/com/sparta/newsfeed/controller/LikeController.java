package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.service.LikeService;
import com.sparta.newsfeed.util.GlobalResponse.GlobalResponseDto;
import com.sparta.newsfeed.util.GlobalResponse.ResponseUtil;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 기능 : like 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class LikeController {

    private final LikeService likeService;

    // 좋아요 기능
    @PostMapping("/{id}/likes")
    public ResponseEntity<GlobalResponseDto> toggleLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean liked = likeService.toggleLike(id, userDetails.getUser().getUsername());

        if (liked) {
            return ResponseUtil.response(StatusCode.LIKE_OK);
        } else {
            return ResponseUtil.response(StatusCode.UNLIKE_OK);
        }
    }
}
