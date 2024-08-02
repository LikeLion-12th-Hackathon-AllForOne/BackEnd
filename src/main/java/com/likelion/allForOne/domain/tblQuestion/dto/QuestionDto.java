package com.likelion.allForOne.domain.tblQuestion.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
public class QuestionDto {
    @Getter
    @Builder
    public static class OrganizeQuestion {
        private String inpDate;          //퀴즈 출제일
        private int questionType;        //퀴즈 유형
        private Long usedQuestionSeq;    //질문코드 구분자
        private String question;         //질문
    }
}
