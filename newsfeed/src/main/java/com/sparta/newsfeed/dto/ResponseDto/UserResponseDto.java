package com.sparta.newsfeed.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 기능 : 유저 관련 응답 Dto
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private int statusCode;
    private String statusMsg;


    public static UserResponseDto res(int statusCode, String statusMsg) {
        return new UserResponseDto(statusCode, statusMsg);
    }
}
