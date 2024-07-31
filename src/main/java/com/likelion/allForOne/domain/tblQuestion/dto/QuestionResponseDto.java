package com.likelion.allForOne.domain.tblQuestion.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class QuestionResponseDto {
    @Getter
    @Builder
    public static class TodayQuestionAndAnswer{
        private QuestionDto.todayQuestion todayQuestion;    //오늘의 질문
        private List<AnswerDto.AnswerForm> saveAnswerList;  //답변
    }
}
