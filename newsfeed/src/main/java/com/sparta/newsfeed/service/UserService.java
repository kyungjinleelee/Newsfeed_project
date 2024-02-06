package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.SignupRequestDto;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.entity.UserRoleEnum;
import com.sparta.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    // 주입 받아오기 (생성자는 @RequiredArgsConstructor로)
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
 //   private final JwtUtil jwtUtil;


    // 관리자 회원가입 인가 방법 : ADMIN_TOKEN (관리자 가입 토큰) 입력 필요, 랜덤하게 생성된 토큰 사용
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 회원 가입
    public void signup(SignupRequestDto requestDto) {   // 회원가입 데이터(requestDto) 받아오기
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword()); // requestDto로 받아온 password 평문을 암호화(encode)해서 password값에 넣어줌

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {    // Optional 내부의 isPresent 메서드 사용
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;    // 일단 일반사용자 권한(USER) 넣어둠
        if (requestDto.isAdmin()) {               // isAdmin: requestDto의 boolean 타입 가져옴
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;            // 관리자 암호가 맞으면 ADMIN 넣어줌
        }
        // 이름
        String name = requestDto.getName();

        // 사용자 등록
        User user = new User(username, password, name, email, role);    // 객체 세팅
        userRepository.save(user);
    }

    // 로그인 (JWT 방식)
//    public void login(LoginRequestDto requestDto, HttpServletResponse res) {
//        String username = requestDto.getUsername();
//        String password = requestDto.getPassword();
//
//        // 1. 사용자 확인
//        User user = userRepository.findByUsername(username).orElseThrow(    // Optional 기능 중 orElseThrow 사용
//                () -> new IllegalArgumentException("등록된 유저가 없습니다.")
//        );
//
//        // 2. 비밀번호 확인
//        if (!passwordEncoder.matches(password, user.getPassword())) {       // matches(입력받은 평문, 암호화 돼 저장된 pwd)
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//        // 사용자 확인 -> 비밀번호 확인까지 됐으면 인증 완료 된거임
//
//        // 3. JWT 생성 > 쿠키에 저장 후 > Response 객체에 추가
//        String token = jwtUtil.createToken(user.getUsername(), user.getRole());
//        jwtUtil.addJwtToCookie(token, res); // 만들어진 토큰 쿠키에 담고, Response 객체에 넣기
//    }
}
