package com.sparta.newsfeed.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 기능 : S3에 저장한 이미지 정보 저장 Entity
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "multimedia")
public class Multimedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;    // image 경로

    public Multimedia(Board board, String fileUrl) {
        this.board = board;
        this.fileUrl = fileUrl;
    }
}
