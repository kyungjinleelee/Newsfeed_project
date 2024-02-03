package com.sparta.newsfeed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequestDto {
    private String username;        // 유저 로그인 아이디 (중복되지 않는 값)
    private String contents;
}
