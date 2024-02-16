package com.sparta.newsfeed.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 기능 : 좋아요 정보 Entity
@Getter
@Entity
@NoArgsConstructor
@Table(name = "`like`")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public Like(User user, Board board) {
        this.user = user;
        this.board = board;
    }
}
