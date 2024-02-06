package com.sparta.newsfeed.entity;

import com.sparta.newsfeed.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 댓글 id (Auto increment)

    @Column(nullable = false, length = 2000)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;    // Board 객체 전체 (외래키 설정)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    public Comment(CommentRequestDto requestDto, User user) {
//        this.contents = requestDto.getContents();
//        this.user = user;
//    }

    public Comment(CommentRequestDto requestDto, Board board, User user) {
        this.contents = requestDto.getContents();
        this.board = board;
        this.user = user;
    }

    public void update(CommentRequestDto requestDto, User user) {
        this.contents = requestDto.getContents();
        this.user = user;
    }
}
