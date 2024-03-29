package com.sparta.newsfeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto incrememt
    private Long id;         // PK 아이디

    @Column(nullable = false, unique = true)    // null값 불가에 중복 허용
    private String username; // 로그인용 아이디

    @Column(nullable = false)
    private String password; // 로그인용 패스워드

 //   @Column(nullable = false)
    private String name;     // 이름

    @Column(nullable = false, unique = true)
    private String email;    // 이메일

    @Column
    private String description; // 한 줄 소개

    @Lob
    private byte[] profile_image;   // 프로필 이미지

    @Column(nullable = false)
    // @Enumerated: EnumType을 DB컬럼에 저장할 때 사용
    // EnumType.STRING 옵션 사용 시 Enum의 이름을 그대로 DB에 저장함
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    private Long kakaoId;       // 카카오 id

    public User(String username, String password, String name, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        // PK(id)는 넣어줄 필요 없음 auto increment 니까
    }

    public User(String username, String password, String name, String email, String description, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.description = description;
        this.role = role;
        // 이미지도 추가해주기 .. -> 타입 뭘로해 ?
    }

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

}
