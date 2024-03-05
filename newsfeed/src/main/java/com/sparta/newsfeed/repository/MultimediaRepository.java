package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 기능 : 이미지 저장 정보 레포
public interface MultimediaRepository extends JpaRepository<Multimedia, Long> {

    void deleteByBoard(Board board);
    List<Multimedia> findByBoardId(Long boardid);
}
