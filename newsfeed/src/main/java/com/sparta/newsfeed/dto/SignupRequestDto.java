package com.sparta.newsfeed.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String username;
    private String password;
    private String name;
    private String email;
    private boolean admin = false;      // admin인지 아닌지 확인 (기본값은 false)
    private String adminToken = "";     // adminToken 기본값은 공백
}
