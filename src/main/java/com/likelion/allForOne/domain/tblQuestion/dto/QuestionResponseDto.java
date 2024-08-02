package com.likelion.allForOne.domain.tblQuestion.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class QuestionResponseDto {
    /**
     * @param questionForm QuestionDto.OrganizeQuestion: 오늘의(출제된) 질문
     * @param answerForm   List<T>: 답변 리스트
     */
    public record QuestionAndAnswer<T>(
            QuestionDto.OrganizeQuestion questionForm,
            List<T> answerForm) {}
}
