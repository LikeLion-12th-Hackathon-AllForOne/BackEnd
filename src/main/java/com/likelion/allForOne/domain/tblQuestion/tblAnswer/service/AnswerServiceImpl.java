package com.likelion.allForOne.domain.tblQuestion.tblAnswer.service;

import com.likelion.allForOne.domain.tblQuestion.tblAnswer.TblAnswerRepository;
import com.likelion.allForOne.domain.tblQuestion.tblAnswer.dto.AnswerDto;
import com.likelion.allForOne.entity.TblAnswer;
import com.likelion.allForOne.entity.TblCode;
import com.likelion.allForOne.entity.TblGroupMember;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AnswerServiceImpl implements AnswerService{
    private final TblAnswerRepository answerRepository;


    /* =================================================================
     * Override
     * ================================================================= */
    /**
     * 답변 정리
     * @param isBasedOnAnswer boolean: true=답변자 기준 / false=대상자 기준
     * @param usedQuestionSeq Long: 오늘의(출제된) 퀴즈 구분자
     * @param memberAnswerEntity Long: 답변자 멤버 entity
     * @param memberTargetEntity TblGroupMember: 대상자 멤버 entity
     * @return AnswerDto.AnswerFormBasic
     */
    @Override
    public AnswerDto.AnswerFormBasic organizeAnswerForm(boolean isBasedOnAnswer, Long usedQuestionSeq, TblGroupMember memberAnswerEntity, TblGroupMember memberTargetEntity) {
        //1. 멤버 구분자
        Long memberAnswerSeq = memberAnswerEntity.getMemberSeq();
        Long memberTargetSeq = memberTargetEntity.getMemberSeq();

        //3. 답변 조회
        Optional<TblAnswer> bfAnswerOpt
                = answerRepository.findByUsedQuestion_UsedQuestionSeqAndMemberAnswer_MemberSeqAndMemberTarget_MemberSeq(usedQuestionSeq, memberAnswerSeq, memberTargetSeq);

        //3. 데이터 반환
        return AnswerDto.AnswerFormBasic.builder()
                .memberAnswerSeq(isBasedOnAnswer ? null : memberAnswerSeq)
                .memberAnswerName(isBasedOnAnswer ? null : findMemberTargetName(memberAnswerEntity))
                .memberTargetSeq(isBasedOnAnswer ? memberTargetSeq : null)
                .memberTargetName(isBasedOnAnswer ? findMemberTargetName(memberTargetEntity) : null)
                .answerSeq(bfAnswerOpt.map(TblAnswer::getAnswerSeq).orElse(null))
                .answerContents(bfAnswerOpt.map(TblAnswer::getAnswerContents).orElse(null))
                .build();
    }

    /* =================================================================
     * 공통 코드
     * ================================================================= */
    private String findMemberTargetName(TblGroupMember memberTarget){
        TblCode codeCategoryRole = memberTarget.getCodeCategoryRole();
        if (codeCategoryRole.getCodeSeq() == 38
                || codeCategoryRole.getCodeSeq() == 39)
            return codeCategoryRole.getCodeName();
        else return memberTarget.getUser().getUserName();
    }


    /* =================================================================
     * 분리 코드
     * ================================================================= */


}
