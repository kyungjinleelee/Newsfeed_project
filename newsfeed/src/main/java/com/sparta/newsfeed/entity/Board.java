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

//    @Column
//    private String boardImg;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    // 글 쓰기
//    public Board(BoardRequestDto requestDto, User user, String boardImg) {
//        this.contents = requestDto.getContents();
//        this.user = user;
//        this.boardImg = boardImg;
//    }

    public Board(BoardRequestDto requestDto, User user) {
        this.contents = requestDto.getContents();
        this.user = user;
    }

    // 업데이트
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
