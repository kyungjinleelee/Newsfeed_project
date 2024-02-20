package com.sparta.newsfeed.dto.ResponseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.newsfeed.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

// 기능 : 유저 관련 디테일 응답 Dto
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDetailDto {

    private Long id;
    private String username;
    private String email;
    private String description;
    private String profileImg;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;
    private String status;

    public static UserDetailDto createUserDetailDto(User user) {
        return new UserDetailDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getDescription(),
                user.getProfileImg(),
                user.getCreatedAt(),
                user.getModifiedAt(),
                user.getStatus()
        );
    }
}
