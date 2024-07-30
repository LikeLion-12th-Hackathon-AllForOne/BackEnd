package com.likelion.allForOne.domain.tblQuestion.service;

import com.likelion.allForOne.entity.TblGroup;
import com.likelion.allForOne.global.response.ApiResponse;

public interface QuestionService {
    void createTodayQuestion(TblGroup entity);  //오늘의 질문 생성하기
    //오늘의 질문 조회하기
    //질문 추가하기
    //답변 임시저장
    //답변 등록
    //지난 퀴즈 모아보기
    //특정 그룹에서 특정인에 대한 퀴즈 모아보기
}
