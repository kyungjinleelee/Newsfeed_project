package com.sparta.newsfeed.service;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.Follow;
import com.sparta.newsfeed.domain.User;
import com.sparta.newsfeed.dto.ResponseDto.FollowingBoardDto;
import com.sparta.newsfeed.repository.BoardRepository;
import com.sparta.newsfeed.repository.FollowRepository;
import com.sparta.newsfeed.repository.UserRepository;
import com.sparta.newsfeed.util.GlobalResponse.CustomException;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "팔로우 Service")
public class FollowService {

    private final FollowRepository followRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 팔로우 하기
    @Transactional
    public boolean toggleFollow(Long followingId, Long followerId) {
        // 팔로잉 유저 정보 체크
        User following = checkUser(followingId);

        // 팔로워 유저 정보 체크
        User follower = checkUser(followerId);

        // 자기 자신 팔로우 요청은 예외 처리
        if (followingId.equals(followerId)) {
            throw new CustomException(StatusCode.CANNOT_FOLLOW_YOURSELF);
        }

        // 팔로우 한번 더 하면 취소
       Optional<Follow> existingFollow = followRepository.findByFollowerIdAndFollowingId(followingId, followerId);
        if(existingFollow.isPresent()) {
            followRepository.deleteByFollowingIdAndFollowerId(followingId, followerId);
            // 팔로우 취소 완료
            return false;
        } else {
            Follow follow = Follow.createFollow(following, follower);
            followRepository.save(follow);
            return true;
        }
    }

    // 내가 팔로우 한 유저 게시글 보기
    public List<FollowingBoardDto> findAll(Long followerId) {

        // 팔로우 목록
        List<Follow> follows = followRepository.findByFollowerId(followerId);

        // 팔로우 목록에서 유저 추출
        List<User> followings = follows.stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());

        // 유저 정보로 board 조회 후 리스트 반환
        List<Board> boards = boardRepository.findByUserIn(followings);
        return boards.stream()
                .map(FollowingBoardDto::createFollowingBoardDto)
                .collect(Collectors.toList());
    }

    // ======================== 메서드 ==============================
    // 유저 정보 확인
    private User checkUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND_USER));
    }


}
