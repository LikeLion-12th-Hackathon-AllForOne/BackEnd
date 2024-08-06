package com.likelion.allForOne.domain.tblLetter;

import com.likelion.allForOne.domain.tblLetter.dto.LetterRequestDto.*;
import com.likelion.allForOne.domain.tblLetter.service.LetterServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/api/letter/searchLetterInfo")
    public ResponseEntity<?> searchLetterInfo(@RequestBody SearchLetterInfo searchLetterInfo, HttpSession session) {
        return ResponseEntity.ok().body(letterService.searchLetterInfo(searchLetterInfo, session));
    }

    // 편지함 조회
    @PostMapping("/api/letter/searchLetterList")
    public ResponseEntity<?> searchLetterList(@RequestBody SearchLetterList searchLetterList, HttpSession session) {
        return ResponseEntity.ok().body(letterService.searchLetterList(searchLetterList, session));
    }

    // 읽음 처리
    @PostMapping("/api/letter/updateReadLetter")
    public ResponseEntity<?> updateReadLetter(@RequestBody UpdateReadLetter updateReadLetter, HttpSession session) {
        return ResponseEntity.ok().body(letterService.updateReadLetter(updateReadLetter, session));
    }
}