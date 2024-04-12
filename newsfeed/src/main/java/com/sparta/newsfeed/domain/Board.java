package com.sparta.newsfeed.domain;

import com.sparta.newsfeed.dto.RequestDto.BoardRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board")
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                                 // 보드 id (Auto increment)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // User 와 연관 관계 설정 (외래키 설정)
    private User user;                               // User 객체 전체

    @Column(columnDefinition = "TEXT")
    private String contents;

    @Column
    private String username;                             // 작성자 아이디

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)     // 연관된 이미지 파일 정보, cascade로 함께 삭제되도록 설정
    private List<Multimedia> multimediaList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Like> boardLike = new ArrayList<>();

    // 글 쓰기
//    public Board(BoardRequestDto requestDto, User user, String boardImg) {
//        this.contents = requestDto.getContents();
//        this.user = user;
//        this.boardImg = boardImg;
//    }

    public Board(BoardRequestDto requestDto, User user) {
        this.contents = requestDto.getContents();
        this.user = user;
        this.username = user.getUsername();
    }
    // 임시
    public Board(List<MultipartFile> files, User user) {
        this.user = user;
        this.id = user.getId();
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
