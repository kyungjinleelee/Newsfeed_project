package com.sparta.newsfeed.security;

import com.sparta.newsfeed.domain.User;
import com.sparta.newsfeed.repository.UserRepository;
import com.sparta.newsfeed.util.GlobalResponse.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.sparta.newsfeed.util.GlobalResponse.code.StatusCode.LOGIN_MATCH_FAIL;

// 기능 : 로그인 시 DB에서 해당 유저 체크
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService { // security의 default 로그인 기능을 사용하지 않겠다

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)     // 해당 유저가 있는지 없는지 확인
                .orElseThrow(() -> new CustomException(LOGIN_MATCH_FAIL)
                );
        System.out.println("user :" + user);
        return new UserDetailsImpl(user, user.getUsername());   // 사용자 정보를 UserDetails로 반환
    }
}