package com.sparta.newsfeed.security;

import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService { // security의 default 로그인 기능을 사용하지 않겠다

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)     // 해당 유저가 있는지 없는지 확인
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
        System.out.println("user :" + user);
        return new UserDetailsImpl(user, user.getUsername());   // 사용자 정보를 UserDetails로 반환
    }
}