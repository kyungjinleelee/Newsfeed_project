package com.sparta.newsfeed.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 기능 : S3에 저장한 이미지 정보 저장 Entity
@Getter
@Entity
@NoArgsConstructor
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // id

    @Column(nullable = false)
    private String path;    // image 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;      // userid

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public ImageFile(String path, User user, Board board) {
        this.path = path;
        this.user = user;
        this.board = board;
    }
}
