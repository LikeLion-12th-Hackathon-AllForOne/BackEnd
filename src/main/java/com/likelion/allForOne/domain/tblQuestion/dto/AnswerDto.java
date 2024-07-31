package com.likelion.allForOne.domain.tblQuestion.dto;

import lombok.Data;
import lombok.Getter;

@Getter
public class AnswerDto {
    @Data
    public static class SaveAnswer {
        private Long answerSeq;         //답변구분자(임시저장값에 대한 저장이나 임시저장시 필요)
        private Long memberTargetSeq;   //질문 대상자 구분자
        private String answerContents;  //답변 내용
    }
}
