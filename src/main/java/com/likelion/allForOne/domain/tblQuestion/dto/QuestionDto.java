package com.likelion.allForOne.domain.tblQuestion.dto;

import com.likelion.allForOne.entity.TblCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

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
    @Getter
    @Builder
    public static class TodayQuestion{
        private int questionStateVal;    //오늘의 퀴즈 상태코드
        private String questionStateMsg; //오늘의 퀴즈 상태메세지
        private int questionType;        //퀴즈 유형
        private Long usedQuestionSeq;    //질문코드 구분자
        private String question;         //질문
    }
}
