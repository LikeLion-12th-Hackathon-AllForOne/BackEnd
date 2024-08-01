package com.likelion.allForOne.domain.tblQuestion.tblAnswer.service;

import com.likelion.allForOne.domain.tblQuestion.tblAnswer.dto.AnswerDto;
import com.likelion.allForOne.entity.TblGroupMember;

public interface AnswerService {
    AnswerDto.AnswerFormBasic organizeAnswerForm(boolean isBasedOnAnswer, Long usedQuestionSeq, TblGroupMember memberAnswerEntity, TblGroupMember memberTargetEntity); //답변 정리
}
