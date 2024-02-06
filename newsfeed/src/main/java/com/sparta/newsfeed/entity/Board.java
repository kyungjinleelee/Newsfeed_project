package com.sparta.newsfeed.entity;

import com.sparta.newsfeed.dto.BoardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "board")
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;             // 보드 id (Auto increment)

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)  // 외래키 설정
    private User user;      // User 객체 전체

    @Column(columnDefinition = "TEXT")
    private String contents;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    public Board(BoardRequestDto requestDto, User user) {
        this.contents = requestDto.getContents();
        this.user = user;
    }

    public Board(BoardRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }

    public void update(BoardRequestDto requestDto, User user) {
        this.contents = requestDto.getContents();
        this.user = user;
    }

//    public static Board of(BoardRequestDto requestDto, User user) {
//        return Board.builder()
//                .requestDto(requestDto)
//                .user(user)
//                .build();
//    }
}
