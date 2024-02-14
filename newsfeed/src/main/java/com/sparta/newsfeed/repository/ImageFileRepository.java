package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.ImageFile;
import com.sparta.newsfeed.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 기능 : 이미지 저장 정보 레포
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {

    List<ImageFile> findAllByBoard(Board board);    // 보드 객체로 글 조회
    List<ImageFile> findAllByUser(User user);       // 해당 유저에 대한 이미지들을 모두 조회
    @Transactional
    void deleteAllByUser(User user);                // 해당 멤버에 대한 모든 이미지 파일을 지움
    @Transactional
    void deleteAllByBoard(Board board);             // 보드 객체로 글 삭제
    boolean existsByUser(User user);                // 해당 유저가 작성한 보드 존재여부 확인
}
