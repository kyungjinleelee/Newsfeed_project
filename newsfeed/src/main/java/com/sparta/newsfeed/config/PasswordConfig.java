package com.sparta.newsfeed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean   // Bean으로 수동등록
    //BCrypt라는 Hash 함수를 사용해서 password를 Encode
    public PasswordEncoder passwordEncoder() { // PasswordEncoder: 비밀번호 암호화할 떄 사용
        return new BCryptPasswordEncoder();    // PasswordEncoder의 구현체 BCryptPasswordEncoder
    }
}
