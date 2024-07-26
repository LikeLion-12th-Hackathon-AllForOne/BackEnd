package com.likelion.allForOne.domain.tblUser;

import com.likelion.allForOne.domain.tblUser.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/api/user/checkIdDuplicate")
    public ResponseEntity<?> checkIdDuplicate(@RequestBody JoinDto userJoinRequestDto) {
        return ResponseEntity.ok().body(userService.checkIdDuplicate(userJoinRequestDto.getUserId()));
    }

    // 로그인
    @PostMapping("/api/user/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpSession session) {
        return ResponseEntity.ok().body(userService.login(loginDto, session));
    }
}
