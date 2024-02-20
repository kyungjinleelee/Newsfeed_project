package com.sparta.newsfeed.dto.RequestDto;

import lombok.Getter;

// 기능 : 회원 탈퇴 Dto
@Getter
public class DeleteUserRequestDto {

    private String username;
    private String password;
}
