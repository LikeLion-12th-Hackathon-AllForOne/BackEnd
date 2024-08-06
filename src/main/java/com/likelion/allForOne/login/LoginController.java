package com.likelion.allForOne.login;

import com.likelion.allForOne.domain.tblUser.dto.UserRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/login")
public class LoginController {
    private final LoginServiceImpl loginService;

    /**
     * 카카오 로그인 (최초)
     * @param dto LoginDto
     * @return ResponseEntity<?>
     */
    @PostMapping("/sign")
    public ResponseEntity<?> firstLogin(@RequestBody UserRequestDto.LoginDto dto) {
        return ResponseEntity.ok().body(loginService.firstLogin(dto));
    }
}
