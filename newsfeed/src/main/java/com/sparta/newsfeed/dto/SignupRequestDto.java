package com.sparta.newsfeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "[a-z0-9].{4,10}", message = "아이디는 4~10자 알파벳 소문자와 숫자로, 4~10자 이내로 구성되어야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
 //   @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[$@$!%*#?&])[A-Za-z\\\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자 알파벳 대소문자, 숫자, 특수문자를 사용하세요.")

    private String password;

    @NotBlank
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank
    private String email;
    private boolean admin = false;      // admin인지 아닌지 확인 (기본값은 false)
    private String adminToken = "";     // adminToken 기본값은 공백
}
