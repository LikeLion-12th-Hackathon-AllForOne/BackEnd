package com.likelion.allForOne.domain.tblQuestion.service;

import com.likelion.allForOne.domain.tblQuestion.dto.QuestionDto;
import com.likelion.allForOne.domain.tblQuestion.dto.QuestionRequestDto;
import com.likelion.allForOne.entity.TblGroup;
import com.likelion.allForOne.global.response.ApiResponse;

import java.time.LocalDate;

public interface QuestionService {
    void createTodayQuestion(LocalDate inpDate, TblGroup entity);  //오늘의 질문 생성하기
    QuestionDto.TodayQuestion findTodayQuestion(TblGroup groupEntity);  //오늘의 질문 조회하기
    ApiResponse<?> addQuestion(QuestionRequestDto.AddQuestion data, Long userSeq);  //질문 추가하기
    ApiResponse<?> saveAnswer(int answerTmpYn, QuestionRequestDto.SaveAnswerList data, Long userSeq);  //답변 (임시)저장 : 임시저장 여부(저장:0 / 임시저장:1)
    ApiResponse<?> findTodayQandA(Long usedQuestionSeq, Long memberSeq, Long userSeq);  //오늘의 질문 답변(임시저장 or 저장)
    ApiResponse<?> findLastQandA(Long groupSeq, Long userSeq, String inpDate);  //지난 퀴즈 모아보기 (전체)
    ApiResponse<?> findByInpDateLastQandA(Long groupSeq, Long userSeq, String inpDate);  //날짜로 지난 퀴즈 조회하기
    ApiResponse<?> findSomeoneQAndA(Long memberTargetSeq, Long userSeq, String inpDate);  //특정 그룹에서 특정인에 대한 퀴즈 모아보기
}
