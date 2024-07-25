package com.likelion.allForOne.domain.tblUser;

import com.likelion.allForOne.domain.tblUser.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<?> join(@RequestBody UserJoinRequestDto userJoinRequestDto) {
        return ResponseEntity.ok().body(userService.join(userJoinRequestDto));
    }
}
