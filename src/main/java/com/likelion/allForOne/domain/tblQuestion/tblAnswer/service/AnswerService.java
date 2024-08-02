package com.likelion.allForOne.domain.tblQuestion.tblAnswer.service;

import com.likelion.allForOne.domain.tblQuestion.tblAnswer.dto.AnswerDto;
import com.likelion.allForOne.entity.TblGroupMember;
import com.likelion.allForOne.entity.TblUsedQuestion;

import java.util.List;

public interface AnswerService {
    List<AnswerDto.BasedOnAnswerForm> findByBasedOnAnswer(TblUsedQuestion usedQuestionEntity, Long memberAnswerSeq, List<TblGroupMember> memberTargetList); //답변자 기준 대상자별 답변 조회
    List<AnswerDto.BasedOnTargetForm> findByBasedOnTarget(boolean isAllQuestion, Long usedQuestionSeq, List<TblGroupMember> memberAnswerList, Long memberTargetSeq); //대상자 기준 답변자별 답변 조회
}
