package com.sparta.newsfeed.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 기능 : 게시글 업로드에 필요한 데이터를 담을 Dto
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequestDto {
    private String username;        // 유저 로그인 아이디 (중복되지 않는 값)
    private String contents;
}
