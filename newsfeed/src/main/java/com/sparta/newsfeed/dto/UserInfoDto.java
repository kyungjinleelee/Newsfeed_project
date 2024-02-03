package com.sparta.newsfeed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    Long id;

    String username;    // 아이디

    String name;
    String email;
    boolean isAdmin;

    public UserInfoDto(String username, boolean isAdmin) {
        this.username = username;
//        this.name = name
//        this.email = email;
        this.isAdmin = isAdmin;
    }
}
