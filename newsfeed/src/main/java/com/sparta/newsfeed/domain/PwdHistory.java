package com.sparta.newsfeed.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 기능 : 변경 한 비밀번호 리스트 히스토리 Entity
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "password_history")
public class PwdHistory extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @Column(name = "old_pwd")
    private String oldPwd;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
