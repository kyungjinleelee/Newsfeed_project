package com.sparta.newsfeed.service;

import com.sparta.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

//    public void userInfo(UserInfoDto userInfoDto) {
//        Long id = userInfoDto.getId();
//        String username = userInfoDto.getUsername();
//        String name = userInfoDto.getName();
//        String description = userInfoDto.getDescription();
//        byte[] profile_image = userInfoDto.getProfile_image();
//
//        // 회원 정보 조회
//        Optional<User> findByUsername = userRepository.findByUsername(username);
//    }

}
