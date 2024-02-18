package com.sparta.newsfeed.domainModel;

import com.sparta.newsfeed.domain.User;
import com.sparta.newsfeed.repository.UserRepository;
import com.sparta.newsfeed.util.GlobalResponse.CustomException;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 기능 : 회원 도메인 관련 DB Read 관리
@Service
@RequiredArgsConstructor
public class UserQuery {

    private final UserRepository userRepository;

    // 유저 Id로 User 객체 찾아오기
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND_USER)
        );
    }


}
