package com.sparta.newsfeed.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.newsfeed.dto.RequestDto.DeleteUserRequestDto;
import com.sparta.newsfeed.dto.RequestDto.SignupRequestDto;
import com.sparta.newsfeed.jwt.JwtUtil;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.service.KakaoService;
import com.sparta.newsfeed.service.UserService;
import com.sparta.newsfeed.util.GlobalResponse.GlobalResponseDto;
import com.sparta.newsfeed.util.GlobalResponse.ResponseUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.newsfeed.util.GlobalResponse.code.StatusCode.DELETE_USER_OK;

@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    // 로그인 페이지
    @GetMapping("/user/login-page")
    public String loginPage() {
        return "login";
    }

    // 회원가입 페이지
    @GetMapping("/user/signup")
    public String signupPage(){
        return "signup";
    }

    // 회원가입 (1 + 2) (예외 처리 전)
//    @PostMapping("/user/signup")
//    public String signup(@ModelAttribute @Valid SignupRequestDto requestDto) {    // 회원가입 성공하면 로그인 페이지 반환할 것이기 때문에 반환타입 String으로 설정
//        userService.signup(requestDto);     // userService.signup에 받아온 request 전달
//
//        return "redirect:/api/user/login-page";      // 로그인 페이지 호출
//    }
    @PostMapping("/user/signup")
    public String signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {    // 회원가입 성공하면 로그인 페이지 반환할 것이기 때문에 반환타입 String으로 설정, @Valid에서 예외 발생 시 BindingResult 객체에 오류에 대한 정보가 담겨서 들어옴
        // Validation 예외 처리
        // 유효성 통과 못한 필드와 메세지를 핸들링
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();  // .getFieldErrors: 오류 발생한 Fields들을 하나씩 가져올 수 있음
        if(!fieldErrors.isEmpty()) {    // 1개 이상인지
            for(FieldError fieldError : bindingResult.getFieldErrors()) {   // List 안의 에러 하나씩 뽑아오기
                // 오류 발생한 부분 로그 확인
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            // 회원가입 페이지로 다시 리턴
            return "redirect:/api/user/signup";
        }

        userService.signup(requestDto);     // userService.signup에 받아온 request 전달
        return "redirect:/api/user/login-page";      // 로그인 페이지 호출
    }

    // 소셜 로그인
    // 1. 카카오에서 보내주는 '인가코드' 받기
    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드 Service 전달 후 인증 처리 및 JWT 반환
        String token = kakaoService.kakaoLogin(code);   // 2. 인가코드로 Service에서 로그인 처리

        // Cookie 생성 및 직접 브라우저에 Set
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";    // 3. 로그인 성공 시 "/"으로 redirect
    }


    // 로그인 (JWT 방식)
//    @PostMapping("/user/login")
//    public String login(@ModelAttribute LoginRequestDto requestDto, HttpServletResponse res){
//        try {
//            userService.login(requestDto, res);
//        } catch (Exception e) {
//            return "redirect:/api/user/login-page?error";
//            // 이렇게 해도 되고 e.getMessage()해서 로그 확인해도 됨
//        }
//
//        return "redirect:/";    // main으로 redirect
//    }

    // 회원 탈퇴
    @DeleteMapping("/user/deleteUser")
    public ResponseEntity<GlobalResponseDto> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody DeleteUserRequestDto deleteUserRequestDto) {
        userService.deleteUser(userDetails.getUser(), deleteUserRequestDto);
        return ResponseUtil.response(DELETE_USER_OK);

    }
}
