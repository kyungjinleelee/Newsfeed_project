package com.sparta.newsfeed.service;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.Like;
import com.sparta.newsfeed.domain.User;
import com.sparta.newsfeed.domainModel.BoardQuery;
import com.sparta.newsfeed.dto.RequestDto.LikeRequestDto;
import com.sparta.newsfeed.repository.LikeRepository;
import com.sparta.newsfeed.repository.UserRepository;
import com.sparta.newsfeed.util.GlobalResponse.CustomException;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final BoardQuery boardQuery;
    private final UserRepository userRepository;

    // 좋아요 기능 메서드
    @Transactional
    public LikeRequestDto like(Long board_id, String username) {
        // 글 정보
        Board board = boardQuery.findBoardById(board_id);

        // 유저 정보
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND_USER));

        // 자신의 글인 경우 좋아요 불가능
        if (username.equals(board.getUser().getUsername())) {
            throw new CustomException(StatusCode.BOARD_OWNER_CANNOT_LIKE);
        }

        // 좋아요 한번 더 하면 취소
        Optional<Like> existingLike = likeRepository.findByUserAndBoard(user, board);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            // 좋아요 취소 완료
            return LikeRequestDto.cancel(existingLike.get());
        } else {
            Like created = likeRepository.save(new Like(user, board));
            return LikeRequestDto.like(created);
        }
    }
}
