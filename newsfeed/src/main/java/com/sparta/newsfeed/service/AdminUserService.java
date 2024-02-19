package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.ResponseDto.UserDetailDto;
import com.sparta.newsfeed.repository.AdminUserRepository;
import com.sparta.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminUserRepository adminUserRepository;

    // 전체 유저 조회하기 (탈퇴 회원 포함)
    public List<UserDetailDto> showAllUser() {

        return adminUserRepository.findAll()
                .stream()
                .map(UserDetailDto::createUserDetailDto)
                .collect(Collectors.toList());
    }


}
