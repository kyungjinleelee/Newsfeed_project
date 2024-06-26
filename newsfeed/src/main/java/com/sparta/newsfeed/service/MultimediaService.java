package com.sparta.newsfeed.service;

import com.sparta.newsfeed.domain.Board;
import com.sparta.newsfeed.domain.Multimedia;
import com.sparta.newsfeed.domain.User;
import com.sparta.newsfeed.domain.UserRoleEnum;
import com.sparta.newsfeed.domainModel.BoardQuery;
import com.sparta.newsfeed.dto.ResponseDto.MultimediaResponseDto;
import com.sparta.newsfeed.repository.BoardRepository;
import com.sparta.newsfeed.repository.MultimediaRepository;
import com.sparta.newsfeed.s3.AwsS3Uploader;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.util.GlobalResponse.CustomException;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MultimediaService {
    private final BoardRepository boardRepository;
    private final BoardQuery boardQuery;
    private final MultimediaRepository multimediaRepository;
    private final AwsS3Uploader awsS3Uploader;

    // 글 쓰기 (이미지)
    @Transactional
    public List<MultimediaResponseDto> createBoardMultimedia(List<MultipartFile> files, UserDetailsImpl userDetails, Long boardId) throws IOException {
        // 해당 글이 DB에 있는지 확인
        Board target = boardQuery.findBoardById(boardId);

        // 글 작성자와 토큰에서 가져온 유저 일치 여부 확인
        if(!target.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new CustomException(StatusCode.NO_AUTH_USER);
        }

        // Multimedia 엔티티 생성 및 db 저장
        createMultimedia(target, files);

        // DB 조회
        List<Multimedia> multimediaList = multimediaRepository.findByBoardId(boardId);

        return multimediaList.stream()
                .map(multimedia -> new MultimediaResponseDto(multimedia.getFileUrl()))
                .collect(Collectors.toList());
    }

    // 글 수정 (이미지)
    @Transactional
    public List<MultimediaResponseDto> updateBoardMultimedia(List<MultipartFile> files, User user, Long boardId) throws IOException {
        Board target = boardQuery.findBoardById(boardId);

        // 글 작성자와 토큰에서 가져온 유저 일치 여부 확인 (관리자라면 수정 가능)
        Optional<Board> found = boardRepository.findByIdAndUser(boardId, user);
        if(found.isEmpty() && user.getRole() == UserRoleEnum.USER) {
            throw new CustomException(StatusCode.NO_AUTH_USER);
        }

        // Multimedia 엔티티 수정 및 db 저장
        createMultimedia(target, files);

        // DB 조회
        List<Multimedia> multimediaList = multimediaRepository.findByBoardId(boardId);

        return multimediaList.stream()
                .map(multimedia -> new MultimediaResponseDto(multimedia.getFileUrl()))
                .collect(Collectors.toList());
    }

    // 글 삭제 (이미지)
    @Transactional
    public List<MultimediaResponseDto> deleteMultimedia(User user, Long boardId) {
        Board target = boardQuery.findBoardById(boardId);

        // 글 작성자와 토큰에서 가져온 유저 일치 여부 확인 (관리자라면 삭제 가능)
        Optional<Board> found = boardRepository.findByIdAndUser(boardId, user);
        if(found.isEmpty() && user.getRole() == UserRoleEnum.USER) {
            throw new CustomException(StatusCode.NO_AUTH_USER);
        }

        // DB 조회 및 삭제
        List<Multimedia> multimediaList = multimediaRepository.findByBoardId(boardId);
        multimediaRepository.deleteByBoard(target);

        return multimediaList.stream()
                .map(multimedia -> new MultimediaResponseDto(multimedia.getFileUrl()))
                .collect(Collectors.toList());
    }

    // 글 이미지 조회
    public List<MultimediaResponseDto> getOneMultimediaList(Long boardId) {
        boardQuery.findBoardById(boardId);

        List<Multimedia> multimediaList = multimediaRepository.findByBoardId(boardId);

        return multimediaList.stream()
                .map(multimedia -> new MultimediaResponseDto(multimedia.getFileUrl()))
                .collect(Collectors.toList());
    }

    // ==================== 메서드 ======================
    private void createMultimedia(Board board, List<MultipartFile> files) throws IOException{
        List<Multimedia> newMultimediaList = new ArrayList<>();

        if (!files.isEmpty()) {
            List<String> fileUrls = awsS3Uploader.uploadMultimedia(files, "static");
            newMultimediaList = fileUrls.stream().map(url -> new Multimedia(board, url)).toList();
        }

        board.getMultimediaList().addAll(newMultimediaList);
        multimediaRepository.saveAll(newMultimediaList);
    }
}
