package com.sparta.newsfeed.jwt;

import com.sparta.newsfeed.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {  // JWT에 관한 기능들을 가진 클래스 (의존하지 않고 모듈로서 동작함)
    // -------------------- JWT 생성 시 필요한 데이터들 -------------------
    // Header KEY 값 (= cookie의 name 값)
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";   // Bearer : 만들 Token 앞에다 붙일 용어. 한 칸 띄어주기 필수!!!!!!
    // 토큰 만료시간 (ms 기준)
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")   // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;  // secretKey를 담을 key 객체
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // LoggerFactory를 사용한 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct  // 딱 한 번만 받아오면 되는 값을 사용할 때마다 요청을 새로 호출하는 실수를 방지하기 위해 사용
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);   // Base64 클래스로 Decoding
        key = Keys.hmacShaKeyFor(bytes);    // key: decode된 secretKey를 담는 객체
    }
    // ----------------------------------------------------------------------
    // 1. JWT 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)   // jwt subject에 사용자 식별자값(ID) 넣었음 (username)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한정보 넣기 (key-value 형식)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간 넣기
                        .setIssuedAt(date) // issuedAt에 발급일 넣기
                        .signWith(key, signatureAlgorithm) // signWith에 secretKey 값 담고있는 key + 암호화 알고리즘 값 넣기
                        .compact();
    }

    // 2. JWT Cookie 에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    // 3. JWT 토큰 substring (Bearer 떼어내기)
    public String substringToken(String tokenValue) {
        // StringUtils.hasText를 사용, 공백과 null 확인 && startsWith을 사용, 토큰의 시작 값이 Bearer이 맞는지 확인
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7); // 맞다면 (true) Bearer 잘라내서 순수 JWT 반환
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 4. substring된 JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;    // Jwts.parserBuilder()를 사용하여 JWT 파싱, JWT가 위변조되지 않았는지 secretKey(key)값을 넣어 확인
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 5. JWT에서 사용자 정보 (claim) 가져오기
    // Jwts.parserBuilder()와 secretKey를 사용하여 JWT의 Claims를 가져와 담겨 있는 사용자의 정보를 사용
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    // -------------------------------------- 끝 -------------------

    // HttpServletRequest 에서 Cookie Value(JWT) 가져오기 (AuthFilter 관련 메서드)
    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;    // 오류가 발생하거나, 해당하는 쿠키 없을 시 null 반환
    }
}
