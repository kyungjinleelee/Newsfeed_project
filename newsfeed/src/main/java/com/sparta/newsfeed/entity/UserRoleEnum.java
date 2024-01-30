package com.sparta.newsfeed.entity;

public enum UserRoleEnum {   // JWT 생성 시, 사용자의 정보로 해당 사용자의 권한을 넣어줄 때 사용
    USER(Authority.USER),    // 사용자 권한
    ADMIN(Authority.ADMIN);  // 관리자 권한

    private final String authority;

    // Authority 안에 값 넣어주기 (내부에 static 클래스 만들어 줘야함)
    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority { // 사용자 권한을 관리하는 inner 클래스
        public static final String USER = "ROLE_USER";  // 권한 규칙에 따라 설정한 값임 (ROLE_**)
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
