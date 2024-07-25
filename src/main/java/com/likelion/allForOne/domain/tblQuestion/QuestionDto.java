package com.likelion.allForOne.domain.tblQuestion;

import com.likelion.allForOne.entity.TblCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionDto {
    @Getter
    @Builder
    public static class todayQuiz {
        private Long usedQuestionSeq;   //질문코드 구분자
        private String question;        //질문
    }
}
