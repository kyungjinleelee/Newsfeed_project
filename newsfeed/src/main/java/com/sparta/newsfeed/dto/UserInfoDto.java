package com.sparta.newsfeed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private Long id;
    private String username;    // 아이디
    private String name;
    private String email;
    private String description;
    private boolean isAdmin;


    public UserInfoDto(String username, String name, String email, boolean isAdmin) {
        this.username = username;
        this.name = name;
        this.email = email;
//        this.description = description;
        this.isAdmin = this.isAdmin;
    }
}
