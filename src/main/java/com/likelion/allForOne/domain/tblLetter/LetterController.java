package com.likelion.allForOne.domain.tblLetter;

import com.likelion.allForOne.domain.tblLetter.dto.LetterRequestDto.*;
import com.likelion.allForOne.domain.tblLetter.service.LetterServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    // 편지 정보 조회
    @GetMapping("/api/letter/searchLetterInfo")
    public ResponseEntity<?> searchLetterInfo(@RequestBody SearchLetterInfo searchLetterInfo, HttpSession session) {
        return ResponseEntity.ok().body(letterService.searchLetterInfo(searchLetterInfo, session));
    }

    // 보낸 편지함 조회
    @GetMapping("/api/letter/searchLetterTo")
    public ResponseEntity<?> SearchLetterTo(@RequestBody SearchLetterTo searchLetterTo, HttpSession session) {
        return ResponseEntity.ok().body(letterService.searchLetterTo(searchLetterTo, session));
    }
}
