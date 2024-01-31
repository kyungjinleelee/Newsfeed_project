package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.SignupRequestDto;
import com.sparta.newsfeed.dto.UserInfoDto;
import com.sparta.newsfeed.entity.UserRoleEnum;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api")
public class UserController {

    // 1. UserService 주입 받기
    private final UserService userService;

    // 2. 생성자로 주입
    public UserController(UserService userService) {
        this.userService = userService;
    }

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
        if(fieldErrors.size() > 0) {    // 1개 이상인지
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

    // 회원 관련 정보 받기
    @GetMapping("/user-info")
    @ResponseBody   // JSON으로 반환
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {  // @AuthenticationPrincipal: 현재 인증된 사용자의 정보를 가져옴
        String username = userDetails.getUser().getUsername();
        UserRoleEnum role = userDetails.getUser().getRole();
        boolean isAdmin = (role == UserRoleEnum.ADMIN);

        return new UserInfoDto(username, isAdmin);
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
}
