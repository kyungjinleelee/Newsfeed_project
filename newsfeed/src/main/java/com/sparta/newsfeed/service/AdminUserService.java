package com.sparta.newsfeed.service;

import com.sparta.newsfeed.domain.User;
import com.sparta.newsfeed.domain.UserRoleEnum;
import com.sparta.newsfeed.domainModel.UserCommand;
import com.sparta.newsfeed.domainModel.UserQuery;
import com.sparta.newsfeed.dto.ResponseDto.UserDetailDto;
import com.sparta.newsfeed.repository.AdminUserRepository;
import com.sparta.newsfeed.util.GlobalResponse.CustomException;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    @Autowired
    private UserQuery userQuery;
    @Autowired
    private UserCommand userCommand;
    @Autowired
    private AdminUserRepository adminUserRepository;


    // 전체 유저 조회 (탈퇴 회원 포함)
    public List<UserDetailDto> showAllUser() {

        return adminUserRepository.findAll()
                .stream()
                .map(UserDetailDto::createUserDetailDto)
                .collect(Collectors.toList());
    }

    // 유저 권한 변경
    public void changeUserRole(Long id, UserRoleEnum newRole) {
        // 유저 정보 조회
        User user = userQuery.findUserById(id);

        // 동일한 권한으로 변경 할 경우 예외 처리
        if (user.getRole().equals(newRole)) {
            throw new CustomException(StatusCode.ALREADY_SAME_ROLE);
        }

        user.setRole(newRole);
        userCommand.saveUser(user);
    }

    // 유저 상태 변경 (강제 탈퇴, 복구)
    public void changeStatus(Long id, String status) {
        // 유저 정보 조회
        User user = userQuery.findUserById(id);

        // 동일한 상태로 변경 할 경우 예외 처리
        if (user.getStatus().equals(status)) {
            throw new CustomException(StatusCode.ALREADY_SAME_STATUS);
        }

        user.setStatus(status);
        userCommand.saveUser(user);
    }
}
