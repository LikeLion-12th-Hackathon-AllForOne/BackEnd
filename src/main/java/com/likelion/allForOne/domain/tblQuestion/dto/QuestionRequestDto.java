package com.likelion.allForOne.domain.tblQuestion.dto;

import lombok.Data;

@Data
public class QuestionRequestDto {
    @Data
    public static class AddQuestion {
        private Long memberSeq;     //멤버 구분자
        private String addQuestion; //질문 내용
    }
}
