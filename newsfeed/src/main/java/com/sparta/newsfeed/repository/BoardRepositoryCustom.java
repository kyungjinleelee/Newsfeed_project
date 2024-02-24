package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<Board> findByOption(String keyword, Pageable pageable);

    Page<Board> findByUser(String username, Pageable pageable);

    Page<Board> findByName(String name, Pageable pageable);
}
