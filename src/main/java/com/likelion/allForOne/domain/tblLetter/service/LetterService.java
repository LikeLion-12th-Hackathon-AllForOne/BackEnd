package com.likelion.allForOne.domain.tblLetter.service;

import com.likelion.allForOne.global.response.ApiResponse;
import com.likelion.allForOne.domain.tblLetter.dto.LetterRequestDto.*;
import jakarta.servlet.http.HttpSession;

public interface LetterService {
    ApiResponse<?> createLetter(CreateLetterDto createLetterDto, HttpSession session); // 편지 등록

    ApiResponse<?> searchLetterInfo(SearchLetterInfo searchLetterInfo, HttpSession session); // 편지 정보 조회

    ApiResponse<?> searchLetterList(SearchLetterList searchLetterList, HttpSession session); // 편지함 조회
    
    ApiResponse<?> updateReadLetter(UpdateReadLetter updateReadLetter, HttpSession session); // 읽음 처리
}
