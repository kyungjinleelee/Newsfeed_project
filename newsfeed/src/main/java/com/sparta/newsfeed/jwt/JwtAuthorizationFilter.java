package com.sparta.newsfeed.jwt;

import com.sparta.newsfeed.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")       // 들어온 JWT를 검증 & 인가
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;                           // JWT 검증을 위해 주입
    private final UserDetailsServiceImpl userDetailsService; // 사용자가 있는지 없는지 체크를 위해 주입

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {    // OncePerRequestFilter 상속 받아서 매개변수로 쓸 수 있음

        String tokenValue = jwtUtil.getTokenFromRequest(req);

        if (StringUtils.hasText(tokenValue)) {
            // JWT 토큰 substring (Bearer 떼주기)
            tokenValue = jwtUtil.substringToken(tokenValue);
            log.info(tokenValue);       // 토큰 제대로 뽑아오는지 확인 차 로그

            if (!jwtUtil.validateToken(tokenValue)) {   // 토큰 검증
                log.error("Token Error");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);     // 토큰 유저 정보 가져오기

            try {
                setAuthentication(info.getSubject());  // '인증 처리' 메서드 (info.getSubject() == 유저 이름 (username))
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }
    // ------------------------------------------------------------------
    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);  // '인증 객체 생성' 메서드
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);  // 인증 객체 생성 시, userDetails 필요함
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
