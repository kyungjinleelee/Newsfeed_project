package com.sparta.newsfeed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/admin")
    public String adminHome() {
        return "admin";
    }

//    @GetMapping("/")
//    public String home(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
//        if(userDetails != null) {
//            String name = userDetails.getUser().getName();
//            model.addAttribute("name", userDetails.getName());  // 유저 이름 동적으로 넣어주기
//        } else {
//            return "redirect:/login";
//        }
//        return "index";
//    }
}
