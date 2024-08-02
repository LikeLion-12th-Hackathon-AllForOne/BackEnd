package com.likelion.allForOne.domain.tblQuestion.tblAnswer.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class AnswerDto {
    @Data
    public static class SaveAnswer {
        private Long answerSeq;         //답변구분자(임시저장값에 대한 저장이나 임시저장시 필요)
        private Long memberTargetSeq;   //질문 대상자 구분자
        private String answerContents;  //답변 내용
    }

    @Getter
    @Builder
    public static class BasedOnAnswerForm {
        private Long memberTargetSeq;    //질문 대상자 구분자
        private String memberTargetName; //질문 대상자 이름
        private int answerTmpYn;         //임시저장 여부(저장:0 / 임시저장:1 / 저장기록 없음:2)
        private Long answerSeq;          //답변구분자 (null 인 경우, 임시저장조차도 한적이 없다는 의미)
        private String answerContents;   //답변 내용
    }

    @Getter
    @Builder
    public static class BasedOnTargetForm {
        private Long memberAnswerSeq;    //질문 답변자 구분자
        private String memberAnswerName; //질문 답변자 이름
        private int answerTmpYn;         //임시저장 여부(저장:0 / 임시저장:1 / 저장기록 없음:2)
        private Long answerSeq;          //답변구분자 (null 인 경우, 임시저장조차도 한적이 없다는 의미)
        private String answerContents;   //답변 내용
    }

    @Getter
    @Builder
    public static class BasedOnAnswerFormList {
        private Long memberAnswerSeq;            //질문 답변자 구분자
        private String memberAnswerName;         //질문 답변자 이름
        private List<BasedOnAnswerForm> memberAnswerList; //답변 리스트
    }
}
