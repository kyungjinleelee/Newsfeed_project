package com.sparta.newsfeed.domainModel;

import com.sparta.newsfeed.domain.User;
import com.sparta.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 기능 : 회원 도메인 관련 DB CUD 관리
@Service
@RequiredArgsConstructor
public class UserCommand {

    private final UserRepository userRepository;

    // 유저 객체 저장하기
    public void saveUser(User user) {
        userRepository.save(user);
    }

    // 유저 객체로 데이터 삭제하기
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    // 회원 탈퇴하며 모든 정보를 정리하기
}
