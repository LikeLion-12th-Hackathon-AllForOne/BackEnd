package com.likelion.allForOne.domain.tblUser;

import com.likelion.allForOne.domain.tblUser.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.likelion.allForOne.domain.tblUser.dto.UserRequestDto.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserServiceImpl userService;

    // 회원가입
    @PostMapping("/api/user/join")
    public ResponseEntity<?> join(@RequestBody JoinDto joinDto) {
        return ResponseEntity.ok().body(userService.join(joinDto));
    }

    // ID 중복체크
    @PostMapping("/api/user/checkIdDuplicate")
    public ResponseEntity<?> checkIdDuplicate(@RequestBody JoinDto userJoinRequestDto) {
        return ResponseEntity.ok().body(userService.checkIdDuplicate(userJoinRequestDto.getUserId()));
    }

    // 로그인
//    @PostMapping("/api/user/login")
//    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, Authentication authentication) {
//        return ResponseEntity.ok().body(userService.login(loginDto, authentication));
//    }

    // 로그아웃
//    @PostMapping("/api/user/logout")
//    public ResponseEntity<?> logout(Authentication authentication) {
//        return ResponseEntity.ok().body(userService.logout(authentication));
//    }

    // 비밀번호 확인
    @PostMapping("/api/user/checkPwd")
    public ResponseEntity<?> checkPwd(@RequestBody CheckPwdDto checkPwdDto, Authentication authentication) {
        return ResponseEntity.ok().body(userService.checkPwd(checkPwdDto, authentication));
    }

    // 내 정보 조회
    @PostMapping("/api/user/searchUserInfo")
    public ResponseEntity<?> searchUserInfo(Authentication authentication) {
        return ResponseEntity.ok().body(userService.searchUserInfo(authentication));
    }

    // 내 정보 수정
    @PostMapping("/api/user/updateUserInfo")
    public ResponseEntity<?> updateUserInfo(@RequestBody UpdateUserInfo updateUserInfo, Authentication authentication) {
        return ResponseEntity.ok().body(userService.updateUserInfo(updateUserInfo, authentication));
    }

    // 회원 탈퇴
    @DeleteMapping("/api/user/deleteUser")
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        return ResponseEntity.ok().body(userService.deleteUser(authentication));
    }

    // 사용자 프로필 변경
    @PostMapping("/api/user/updateUserImage")
    public ResponseEntity<?> updateUserImage(@RequestBody UpdateUserImage updateUserImage, Authentication authentication) {
        return ResponseEntity.ok().body(userService.updateUserImage(updateUserImage, authentication));
    }
}
