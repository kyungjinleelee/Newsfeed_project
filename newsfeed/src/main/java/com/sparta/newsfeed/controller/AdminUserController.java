package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.domain.UserRoleEnum;
import com.sparta.newsfeed.dto.ResponseDto.UserDetailDto;
import com.sparta.newsfeed.service.AdminUserService;
import com.sparta.newsfeed.util.GlobalResponse.GlobalResponseDto;
import com.sparta.newsfeed.util.GlobalResponse.ResponseUtil;
import com.sparta.newsfeed.util.GlobalResponse.code.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminUserController {

    private final AdminUserService adminUserService;

    // 전체 유저 조회
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allUser")
    public ResponseEntity<List<UserDetailDto>> showAllUser() {
        return ResponseUtil.response(adminUserService.showAllUser());
    }

    // 유저 Role 변경
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/changeRole")
    public ResponseEntity<GlobalResponseDto> userToAdmin(@PathVariable Long id,
                                                         @RequestParam("role") UserRoleEnum role) {

        if (role.equals(UserRoleEnum.ADMIN)) {
            // 관리자로 변경
            adminUserService.changeUserRole(id, UserRoleEnum.ADMIN);
        } else {
            // 유저로 변경
            adminUserService.changeUserRole(id, UserRoleEnum.USER);
        }
        return ResponseUtil.response(StatusCode.CHANGE_ROLE_OK);
    }

    // 유저 강제 탈퇴 및 복구
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<GlobalResponseDto> deleteUser(@PathVariable Long id,
                                                        @RequestParam("status") String status) {
        if (status.equals("N")) {
            // 강퇴
            adminUserService.changeStatus(id, status);
        }
        if (status.equals("Y")) {
            // 복구
            adminUserService.changeStatus(id, status);
        }
        return ResponseUtil.response(StatusCode.CHANGE_USER_STATUS_OK);
    }
}
