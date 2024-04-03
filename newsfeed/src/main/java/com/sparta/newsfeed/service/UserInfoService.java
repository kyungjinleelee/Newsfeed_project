package com.sparta.newsfeed.service;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.PwdHistory;
import com.sparta.newsfeed.domain.User;
import com.sparta.newsfeed.domainModel.UserQuery;
import com.sparta.newsfeed.dto.RequestDto.PwdUpdateDto;
import com.sparta.newsfeed.dto.RequestDto.SignupRequestDto;
import com.sparta.newsfeed.dto.ResponseDto.BoardResponseDto;
import com.sparta.newsfeed.dto.ResponseDto.PrivateResponseBody;
import com.sparta.newsfeed.repository.BoardRepository;
import com.sparta.newsfeed.repository.HistoryRepository;
import com.sparta.newsfeed.repository.UserRepository;
import com.sparta.newsfeed.s3.AwsS3Uploader;
import com.sparta.newsfeed.util.GlobalResponse.CustomException;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

// 기능 : 회원 정보 Service
@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoService {
    private final UserQuery userQuery;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final HistoryRepository historyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AwsS3Uploader awsS3Uploader;

    // 닉네임 변경
    @Transactional
    public PrivateResponseBody changeName(SignupRequestDto signupRequestDto, User user) {

        User user1 = userQuery.findUserById(user.getId());

        if(user.getId().equals(user1.getId())){
            user1.update(signupRequestDto);
            return new PrivateResponseBody<>(StatusCode.OK,"닉네임 변경 완료");
        } else {
            throw new CustomException(StatusCode.LOGIN_MATCH_FAIL);
        }
    }

    // 한 줄 소개 수정
    @Transactional
    public PrivateResponseBody changeDescription(SignupRequestDto signupRequestDto, User user) {
        User user1 = userQuery.findUserById(user.getId());

        if(user.getId().equals(user1.getId())) {
            user1.updateDescription(signupRequestDto);
            return new PrivateResponseBody<>(StatusCode.OK, "한줄소개 수정 완료");
        } else {
            throw new CustomException(StatusCode.LOGIN_MATCH_FAIL);
        }
    }

    // 프로필 사진 수정
    @Transactional
    public PrivateResponseBody changeProfile(User user, MultipartFile image) throws IOException {
        User user1 = userQuery.findUserById(user.getId());

        if(user.getId().equals(user1.getId())) {
            String profileImg = awsS3Uploader.uploadProfile(image, "profileImg");

            // 유저 수정
            user1.updateProfileImg(profileImg);

            userRepository.save(user1);

            return new PrivateResponseBody<>(StatusCode.OK, "프로필 사진 수정 완료");
        } else {
            throw new CustomException(StatusCode.LOGIN_MATCH_FAIL);
        }
    }

    // 내가 쓴 게시글 조회
    public List<BoardResponseDto> getBoardsByUserId(User user) {
        User user1 = userQuery.findUserById(user.getId());

        // 내가 쓴 글 작성된 순 대로 내림차순으로 가져옴
        List<Board> boards = boardRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        // Dto로 변환하여 반환
        return boards.stream()
                .map(BoardResponseDto::createBoardDto)
                .collect(Collectors.toList());
    }

    // 비밀번호 수정
    @Transactional
    public PwdUpdateDto updatePwd(Long id, PwdUpdateDto pwdUpdateDto) {

        pwdUpdateDto.setId(id);                     // userid
        String oldPwd = pwdUpdateDto.getOldPwd();   // 현재 비밀번호 입력

        User target = userRepository.findById(id)
                .orElseThrow(() ->
                        new CustomException(StatusCode.NOT_FOUND_USER)
                );

        // 기존 등록된 비밀번호와 현재 비밀번호 입력값이 일치하는지 확인
        String currentPwd = target.getPassword();

        if(!passwordEncoder.matches(oldPwd, currentPwd)) {
            throw new CustomException(StatusCode.PWD_MATCH_FAIL);
        }

        // =============== 최근 3번 안에 사용한 비밀번호 사용할 수 없도록 하는 로직 ============
        // 1. 패스워드 최근 3개 조회 + 새로운 비밀번호와 일치 여부
        List<PwdHistory> pwds = showPwd(id);
        String newPwd = pwdUpdateDto.getNewPwd();

        boolean newPasswordValid = pwds
                .stream()
                .noneMatch(history -> passwordEncoder.matches(newPwd, history.getOldPwd()));

        if (newPasswordValid) {
            // 2. 패스워드 수정 전에 히스토리 저장
            saveHistory(target, currentPwd);

            // 패스워드 수정
            pwdUpdateDto.setNewPwd(passwordEncoder.encode(newPwd));
            target.updatePwd(pwdUpdateDto);

            // 3. 새로운 비밀번호로 업데이트
            User updated = userRepository.save(target);

            // Entity -> Dto
            return PwdUpdateDto.create(updated);
        } else {
            throw new CustomException(StatusCode.RECENT_PASSWORD_USED);
        }
        // ======================= 로직 끝 ========================
    }

    // ======================== 메서드 ===========================
    // 비밀번호 최근 3개 조회
    private List<PwdHistory> showPwd(Long id) {
        List<PwdHistory> passwords = historyRepository.findByUserId(id);
        return passwords;
    }

    // 비밀번호 히스토리 테이블에 저장
    @Transactional
    public void saveHistory(User user, String password) {
        PwdHistory history = new PwdHistory();
        history.setUser(user);
        history.setOldPwd(password);
        historyRepository.save(history);
    }

}
