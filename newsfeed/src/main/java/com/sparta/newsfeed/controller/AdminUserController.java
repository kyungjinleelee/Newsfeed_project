package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.ResponseDto.UserDetailDto;
import com.sparta.newsfeed.service.AdminUserService;
import com.sparta.newsfeed.util.GlobalResponse.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminUserController {

    private final AdminUserService adminUserService;

    // 전체 유저 조회
    @GetMapping("/allUser")
    public ResponseEntity<List<UserDetailDto>> showAllUser() {
     //   List<UserDetailDto> users = adminUserService.showAllUser();
        return ResponseUtil.response(adminUserService.showAllUser());
    }
}
