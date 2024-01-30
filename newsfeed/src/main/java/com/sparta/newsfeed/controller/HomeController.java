package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

//    @GetMapping("/")
//    public String home(Model m) {
//        m.addAttribute("username", "username");
//        return "index";
//    }

    @GetMapping("/")
    public String home(Model m, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        m.addAttribute("name", userDetails.getName());  // 유저 이름 동적으로 넣어주기
        return "index";
    }
}
