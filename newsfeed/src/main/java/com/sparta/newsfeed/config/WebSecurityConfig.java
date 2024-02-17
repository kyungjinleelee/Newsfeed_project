package com.sparta.newsfeed.config;

import com.sparta.newsfeed.jwt.JwtUtil;
import com.sparta.newsfeed.security.JwtAuthenticationFilter;
import com.sparta.newsfeed.security.JwtAuthorizationFilter;
import com.sparta.newsfeed.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;    // 이 두 가지 값 Filter에 넣어줘야 하기 때문에, 생성자로 주입 받아옴
    private final AuthenticationConfiguration authenticationConfiguration;  // Authentication Manager 만들기 위해 주입 받아옴

    // ------------------------ 1. Bean으로 직접 수동 등록해서 필터 만들기 ----------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean   // authenticationManager는 AuthenticationConfiguration을 통해서만 생성 가능함
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean   // JwtAuthentication 필터 Bean으로 등록하는 메서드
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);  // jwt 필터 객체 생성
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration)); // AuthenticationManager 세팅
        return filter;  // 세팅된 것 반환
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    // ------------------------ 2. 등록 후 security Filter에 끼워 넣기 ------------------
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)    // SessionCreationPolicy을 STATELESS로 줄거임!
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/").permitAll() // 메인 페이지 요청 허가
                        .requestMatchers("/api/user/**").permitAll() // '/api/user/'로 시작하는 요청 모두 접근 허가
                        .requestMatchers(HttpMethod.GET, "/api/boards/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/follow/board").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/boards").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/resource").permitAll()
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        http.formLogin((formLogin) ->
                formLogin
                        .loginPage("/api/user/login-page").permitAll()  // 직접 만든 로그인 페이지 사용 위해서 경로 설정 추가
        );

        // 필터 관리 (인가 필터 -> 인증 필터 순으로 처리)
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 접근 불가 페이지 설정 (관리자 페이지용)
//        http.exceptionHandling((exceptionHandling) ->
//                exceptionHandling
//                        .accessDeniedPage("/forbidden.html")    // Denied 됐을 때, forbidden.html로 이동
//
//        );

        return http.build();
    }
}