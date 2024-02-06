package com.sparta.newsfeed.dto;

import com.sparta.newsfeed.entity.User;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private Long id;

    @Pattern(regexp = "[a-z0-9]{4,10}", message = "이름은 알파벳 소문자, 숫자를 포함하여 4~10자여야 합니다.")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "이메일 양식을 지켜 작성해 주세요.")
    private String email;
    private String description;

    public static UserRequestDto createUser(User user) {
        return new UserRequestDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getDescription()
        );
    }
}
