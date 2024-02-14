package com.sparta.newsfeed.jwt;

import com.sparta.newsfeed.domain.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
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
    // 2. header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);   // Bearer 달려있는 토큰 값 가져옴
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);                  // substring으로 잘라서 순수한 값만 사용
        }
        return null;
    }

    // 3. JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 4. JWT에서 사용자 정보 (claim) 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    // -------------------------------------- 끝 -------------------
}
