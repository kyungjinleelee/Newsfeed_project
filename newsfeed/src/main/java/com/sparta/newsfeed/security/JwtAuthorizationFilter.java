package com.sparta.newsfeed.security;

import com.sparta.newsfeed.jwt.JwtUtil;
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

        String tokenValue = jwtUtil.getJwtFromHeader(req);  // 1. 순수한 토큰을 바로 뽑아낼 수 있음

        if (StringUtils.hasText(tokenValue)) {              // 2. hasText로 확인

            if (!jwtUtil.validateToken(tokenValue)) {       // 3. 토큰 검증
                log.error("Token Error");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);     // 4. 토큰 유저 정보 가져오기

            try {
                setAuthentication(info.getSubject());  // 5. Authentication 인증 처리하는 메서드로 인증처리 (info.getSubject() == 유저 이름 (username))
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }
    // ------------------------------------------------------------------
    // 7. 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);  // 6. 인증 객체 생성 메서드로 생성해서 넣어줌
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);      // 8. ContextHolder에 세팅
    }

    // 6. 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);  // 인증 객체 생성 시, userDetails 필요함
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
