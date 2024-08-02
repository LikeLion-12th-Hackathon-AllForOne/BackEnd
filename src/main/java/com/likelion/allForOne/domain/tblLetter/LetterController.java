package com.likelion.allForOne.domain.tblLetter;

import com.likelion.allForOne.domain.tblLetter.dto.LetterRequestDto.*;
import com.likelion.allForOne.domain.tblLetter.service.LetterServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LetterController {
    private final LetterServiceImpl letterService;

    // 편지 등록
    @PostMapping("/api/letter/createLetter")
    public ResponseEntity<?> createLetter(@RequestBody CreateLetterDto createLetterDto, HttpSession session) {
        return ResponseEntity.ok().body(letterService.createLetter(createLetterDto, session));
    }
}
