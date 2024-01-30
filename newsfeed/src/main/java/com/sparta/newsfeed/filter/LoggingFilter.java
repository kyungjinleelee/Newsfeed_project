package com.sparta.newsfeed.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@Slf4j(topic = "LoggingFilter")
// @Component
@Order(1)   // 첫 번째로 수행
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 전처리
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI();    // url 정보 가져오기
        log.info(url);      // 1. 어떤 요청인지 @Slf4j 이용해 로그 찍기

        chain.doFilter(request, response); // 2. 다음 Filter 로 이동

        // 후처리
        log.info("비즈니스 로직 완료");       // 3. 일련의 로직이 끝난 후 로그 찍힘
    }
}
