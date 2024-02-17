package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.UserInfoDto;
import com.sparta.newsfeed.domain.UserRoleEnum;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.service.UserInfoService;
import com.sparta.newsfeed.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
@Slf4j(topic = "회원 정보 컨트롤러")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final UserService userService;

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

    // 회원 정보 조회
    @GetMapping("/user/info")
    public String getUserInfo() {
        log.info("회원 정보 뷰");
        return "user-info";
    }



//    @GetMapping("/user-info")
//    public String getUserInfo(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        log.info("회원 정보 불러오는 시도");
//        String username = userDetails.getUser().getUsername();
//        String name = userDetails.getUser().getName();
//        String email = userDetails.getUser().getEmail();
//        UserRoleEnum role = userDetails.getUser().getRole();
//        boolean isAdmin = (role == UserRoleEnum.ADMIN);
//
////        UserInfoDto user = UserInfoService.findUser(userDetails.getUsername());
//        UserInfoDto userInfoDto = new UserInfoDto(username, name, email, isAdmin);
//        model.addAttribute("userInfo", userInfoDto);
//
//        return "userInfo";
//    }
}
