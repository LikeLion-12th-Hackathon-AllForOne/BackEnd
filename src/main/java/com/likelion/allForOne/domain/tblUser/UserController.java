package com.likelion.allForOne.domain.tblUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.likelion.allForOne.domain.tblUser.UserRequestDto.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/api/user/join")
    public ResponseEntity<?> join(@RequestBody UserJoinRequestDto userJoinRequestDto) {
        log.info("회원 가입 요청");
        return ResponseEntity.ok().body(userService.join(userJoinRequestDto));
    }
}
