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

    // 기본 정보 조회
//   public UserResponseDto findUser(String username){
//       User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));
//   }

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

    // 기본 프로필 수정
//    @Transactional
//    public UserRequestDto updateUserinfo(Long id, UserRequestDto updateDto) {
//
//    }

}
