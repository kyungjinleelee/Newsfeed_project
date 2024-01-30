package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.SignupRequestDto;
import com.sparta.newsfeed.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    // 회원가입 (1 + 2)
    @PostMapping("/user/signup")
    public String signup(@ModelAttribute SignupRequestDto requestDto) {    // 회원가입 성공하면 로그인 페이지 반환할 것이기 때문에 반환타입 String으로 설정
        userService.signup(requestDto);     // userService.signup에 받아온 request 전달

        return "redirect:/api/user/login-page";      // 로그인 페이지 호출
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
