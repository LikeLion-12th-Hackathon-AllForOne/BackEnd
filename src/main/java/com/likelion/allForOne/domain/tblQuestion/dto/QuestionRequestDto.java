package com.likelion.allForOne.domain.tblQuestion.dto;

import com.likelion.allForOne.domain.tblQuestion.tblAnswer.dto.AnswerDto;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRequestDto {
    @Data
    public static class AddQuestion {
        private Long memberSeq;     //멤버 구분자
        private String addQuestion; //질문 내용
    }
    @Data
    public static class SaveAnswerList {
        private Long questionSeq;       //질문 구분자
        private Long memberAnswerSeq;   //답변 생성자 구분자
        private List<AnswerDto.SaveAnswer> saveAnswerList;  //답변 리스트(질문 대상자&답변내용)
    }
}
