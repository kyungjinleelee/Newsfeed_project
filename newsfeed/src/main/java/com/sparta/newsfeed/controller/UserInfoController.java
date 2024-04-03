package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.domain.UserRoleEnum;
import com.sparta.newsfeed.dto.RequestDto.PwdUpdateDto;
import com.sparta.newsfeed.dto.RequestDto.SignupRequestDto;
import com.sparta.newsfeed.dto.ResponseDto.BoardResponseDto;
import com.sparta.newsfeed.dto.ResponseDto.PrivateResponseBody;
import com.sparta.newsfeed.dto.UserInfoDto;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.service.UserInfoService;
import com.sparta.newsfeed.service.UserService;
import com.sparta.newsfeed.util.GlobalResponse.ResponseUtil;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/api")
@Slf4j(topic = "회원 정보 컨트롤러")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final UserService userService;

    // 회원 정보 페이지 이동
    @GetMapping("/user/info")
    public String getUserInfo() {
        log.info("회원 정보 뷰");
        return "user-info";
    }

    // 회원 정보 조회
    @GetMapping("/user-info")
    @ResponseBody   // JSON으로 반환
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {  // @AuthenticationPrincipal: 현재 인증된 사용자의 정보를 가져옴
        log.info("회원 정보 불러오는 시도");
        String username = userDetails.getUser().getUsername();
        String name = userDetails.getUser().getName();
        String email = userDetails.getUser().getEmail();
        UserRoleEnum role = userDetails.getUser().getRole();
        boolean isAdmin = (role == UserRoleEnum.ADMIN);
        return new UserInfoDto(username, name, email, isAdmin);
    }

    // 닉네임 변경
    @PutMapping("/user-info/changeName")
    public ResponseEntity<PrivateResponseBody> changeName(@RequestBody SignupRequestDto signupRequestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtil.response(userInfoService.changeName(signupRequestDto, userDetails.getUser()));
    }

    // 한 줄 소개 수정
    @PutMapping("/user-info/changeDescription")
    public ResponseEntity<PrivateResponseBody> changeDescription(@RequestBody SignupRequestDto signupRequestDto,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtil.response(userInfoService.changeDescription(signupRequestDto, userDetails.getUser()));
    }

    // 프로필 사진 수정
    @PutMapping("/user-info/changeProfile")
    public ResponseEntity<PrivateResponseBody> changeProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @RequestPart(value = "file") MultipartFile image) throws IOException {
        return ResponseUtil.response(userInfoService.changeProfile(userDetails.getUser(), image));
    }

    // 비밀번호 수정
    @PutMapping("/user-info/{id}")      // userid
    public ResponseEntity<PrivateResponseBody> updatePwd(@PathVariable Long id,
                                                     @RequestBody @Valid PwdUpdateDto pwdUpdateDto,
                                                     BindingResult bindingResult) {
        // Validation 예외 처리
        // 유효성 통과 못한 필드와 메세지를 핸들링
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            // 에러 메세지를 포함한 PrivateResponseBody 생성
            PrivateResponseBody response = new PrivateResponseBody(StatusCode.PWD_UPDATE_FAIL, errorMessages);
            return ResponseUtil.response(response);
        }
        // 비밀번호 업데이트
        userInfoService.updatePwd(id, pwdUpdateDto);

        PrivateResponseBody response = new PrivateResponseBody(StatusCode.OK, "비밀번호 수정 완료.");
        return ResponseUtil.response(response);
    }

    // 내가 쓴 게시글 조회
    @GetMapping("/user-info/boards")
    @ResponseBody
    public ResponseEntity<List<BoardResponseDto>> getBoardsByUserId(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtil.response(userInfoService.getBoardsByUserId(userDetails.getUser()));
    }

}
