package com.sparta.newsfeed.dto.RequestDto;

import com.sparta.newsfeed.domain.User;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

// 기능 : 비밀번호 변경 요청 Dto
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PwdUpdateDto {

    private Long id;        // userid

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$^!%*#?&])[A-Za-z\\d$@$^!%*#?&]{8,15}$", message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자를 포함하여 8~15자여야 합니다.")
    private String oldPwd;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$^!%*#?&])[A-Za-z\\d$@$^!%*#?&]{8,15}$", message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자를 포함하여 8~15자여야 합니다.")
    private String newPwd;

    private LocalDateTime modifiedAt;

    // 새로운 비밀번호만을 받는 생성자 추가
    public PwdUpdateDto (Long id, String newPwd) {
        this.id = id;
        this.newPwd = newPwd;
    }

    public static PwdUpdateDto create(User user) {
        // 새로운 비밀번호만 반환
        return new PwdUpdateDto(user.getId(), user.getPassword());
    }

}
