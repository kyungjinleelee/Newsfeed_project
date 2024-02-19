package com.sparta.newsfeed.util.GlobalResponse.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 기능 : 응답용 메세지 커스텀
@Getter
public enum StatusCode {

    // ====================== 예외 응답 코드 ========================
    // 400 BAD_REQUEST : 잘못된 요청
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "요청이 올바르지 않습니다."),
    LOGIN_MATCH_FAIL(HttpStatus.BAD_REQUEST, "400", "회원을 찾을 수 없습니다."),
    USERID_MATCH_FAIL(HttpStatus.NOT_FOUND, "404", "요청 USER ID가 잘못 입력되었습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "404", "회원을 찾을 수 없습니다."),
    PWD_MATCH_FAIL(HttpStatus.BAD_REQUEST,"400", "비밀번호가 일치하지 않습니다."),
    PWD_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "400", "비밀번호 수정에 실패했습니다." ),
    RECENT_PASSWORD_USED(HttpStatus.BAD_REQUEST, "400", "최근 3번 안에 사용한 비밀번호는 사용할 수 없습니다."),
    FILE_DELETE_FAILED(HttpStatus.NOT_FOUND, "404", "파일 삭제 실패"),
    FILE_CONVERT_FAILED(HttpStatus.NOT_FOUND, "404", "파일 전환 실패"),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 게시글이 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "댓글이 존재하지 않습니다."),
    NO_AUTH_USER(HttpStatus.BAD_REQUEST, "403", "작성자 정보와 일치하지 않습니다."),
    COMMENT_OWNER_CANNOT_LIKE(HttpStatus.BAD_REQUEST, "403", "자신이 작성한 댓글에는 좋아요 할 수 없습니다."),
    BOARD_OWNER_CANNOT_LIKE(HttpStatus.BAD_REQUEST, "403", "자신이 작성한 글에는 좋아요 할 수 없습니다."),
    CANNOT_FOLLOW_YOURSELF(HttpStatus.BAD_REQUEST, "403", "자기 자신을 팔로우 할 수 없습니다."),
    CONFLICT_LIKE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "409", "이미 좋아요 한 글입니다."),
    // ====================== 성공 응답 코드 =========================
    OK(HttpStatus.OK, "200", "응답이 정상 처리 되었습니다."),
    PWD_UPDATE_OK(HttpStatus.OK, "200", "비밀번호 수정 성공했습니다."),
    DELETE_OK(HttpStatus.OK, "200", "삭제 성공했습니다."),
    LIKE_OK(HttpStatus.OK, "200", "좋아요 성공했습니다."),
    FOLLOW_OK(HttpStatus.OK, "200", "팔로우 성공했습니다."),
    IMG_UPDATE_OK(HttpStatus.OK, "200", "프로필 이미지가 성공적으로 수정됐습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String statusCode;
    private final String statusMsg;

    StatusCode(HttpStatus httpStatus, String statusCode, String statusMsg) {
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }
}
