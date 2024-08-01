package com.likelion.allForOne.domain.tblQuestion.tblAnswer.service;

import com.likelion.allForOne.domain.tblQuestion.tblAnswer.dto.AnswerDto;
import com.likelion.allForOne.entity.TblGroupMember;

public interface AnswerService {
    AnswerDto.BasedOnAnswerForm organizeBasedOnAnswerForm(Long usedQuestionSeq, Long memberAnswerSeq, TblGroupMember memberTargetEntity); //답변자 기준 대상자별 답변 조회
    AnswerDto.BasedOnTargetForm organizeBasedOnTargetForm(Long usedQuestionSeq, TblGroupMember memberAnswerEntity, Long memberTargetSeq); //대상자 기준 답변자별 답변 조회
}
