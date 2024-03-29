package com.sparta.newsfeed.security;

import com.sparta.newsfeed.domain.User;
import com.sparta.newsfeed.domain.UserRoleEnum;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 기능 : 유저 디테일 구현체
@Getter
public class UserDetailsImpl implements UserDetails {   // security의 default 로그인 기능을 사용하지 않겠다

    private final User user;            // 인증 완료된 User 객체
    private final String username;
//    private final String username;
//    private final String password;

    public UserDetailsImpl(User user, String username) {
        this.user = user;
        this.username = username;
//      this.password = password;
    }
    public User getUser() { return this.user; }       // 인증 완료된 User를 가져오는 Getter

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 권한 설정 및 접근 불가 페이지
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}