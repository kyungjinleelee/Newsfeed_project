package com.sparta.newsfeed.util.GlobalResponse;

import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.Getter;

// 기능 : 실행 예외에 ErrorCode 필드를 추가해 커스텀
@Getter
public class CustomException extends RuntimeException {     // 실행 예외 클래스를 상속받아서 Unchecked Exception으로 활용
    private final StatusCode statusCode;

    public CustomException(StatusCode statusCode) {
        super(statusCode.getStatusMsg());
        this.statusCode = statusCode;
    }
}
