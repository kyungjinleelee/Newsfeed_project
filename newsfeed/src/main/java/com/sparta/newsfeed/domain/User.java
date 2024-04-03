package com.sparta.newsfeed.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sparta.newsfeed.dto.RequestDto.PwdUpdateDto;
import com.sparta.newsfeed.dto.RequestDto.SignupRequestDto;
import com.sparta.newsfeed.util.GlobalResponse.CustomException;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto incrememt
    private Long id;         // PK 아이디

    @Column(nullable = false, unique = true)
    private String username; // 로그인용 아이디

    @Column(nullable = false)
    private String password; // 로그인용 패스워드

 //   @Column(nullable = false)
    private String name;     // 닉네임

    @Column(nullable = false, unique = true)
    private String email;    // 이메일

    @Column
    private String description; // 한 줄 소개

    @Column
    private String profileImg;   // 프로필 이미지

    @Column(columnDefinition = "varchar(1) default 'Y'")
    private String status;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    private Long kakaoId;

    public User(String username, String password, String name, String email, String status, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.status = status;
        this.role = role;
        // PK(id)는 넣어줄 필요 없음 auto increment 니까
    }

//    public User(String username, String password, String name, String email, String description, UserRoleEnum role) {
//        this.username = username;
//        this.password = password;
//        this.name = name;
//        this.email = email;
//        this.description = description;
//        this.role = role;
//    }

    public User(String username, String password, String email, UserRoleEnum role, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId =kakaoId;
    }
    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    // 닉네임 수정
    public void update(SignupRequestDto signupRequestDto) {
        this.name = signupRequestDto.getName();
    }

    // 한 줄 소개 수정
    public void updateDescription(SignupRequestDto signupRequestDto) {
        this.description = signupRequestDto.getDescription();
    }

    // 프로필 사진 수정
    public void updateProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    // 비밀번호 수정
    public void updatePwd(PwdUpdateDto pwdUpdateDto) {
        if (!this.id.equals(pwdUpdateDto.getId()))
            throw new CustomException(StatusCode.USERID_MATCH_FAIL);

        this.password = pwdUpdateDto.getNewPwd();
    }

}
