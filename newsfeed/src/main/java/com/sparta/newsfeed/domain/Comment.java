package com.sparta.newsfeed.domain;

import com.sparta.newsfeed.dto.RequestDto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;         // 댓글 id (Auto increment)

    @Column(nullable = false)
    private String username;    // 닉네임

    @Column(nullable = false, length = 2000)
    private String comment; // 댓글 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 대댓글
    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;    // Board 객체 전체 (외래키 설정)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;      // User 객체 전체 (외래키 설정)


    public Comment(CommentRequestDto requestDto, Board board, User user) {
        this.username = user.getUsername();
        this.comment = requestDto.getComment();
        this.board = board;
        this.user = user;
    }

    // 대댓글
    public Comment(CommentRequestDto requestDto, User user, Board board, Comment parent) {
        this.username = user.getUsername();
        this.comment = requestDto.getComment();
        this.parent = parent;
        this.user = user;
        this.board = board;
    }

    public void update(CommentRequestDto requestDto, User user) {
        this.comment = requestDto.getComment();
        this.user = user;
    }
}
