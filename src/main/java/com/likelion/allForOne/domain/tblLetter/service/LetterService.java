package com.likelion.allForOne.domain.tblLetter.service;

import com.likelion.allForOne.global.response.ApiResponse;
import com.likelion.allForOne.domain.tblLetter.dto.LetterRequestDto.*;
import org.springframework.security.core.Authentication;

public interface LetterService {
    ApiResponse<?> createLetter(CreateLetterDto createLetterDto, Authentication authentication); // 편지 등록

    ApiResponse<?> searchLetterInfo(SearchLetterInfo searchLetterInfo, Authentication authentication); // 편지 정보 조회

    ApiResponse<?> searchLetterList(SearchLetterList searchLetterList, Authentication authentication); // 편지함 조회
    
    ApiResponse<?> updateReadLetter(UpdateReadLetter updateReadLetter, Authentication authentication); // 읽음 처리
}
