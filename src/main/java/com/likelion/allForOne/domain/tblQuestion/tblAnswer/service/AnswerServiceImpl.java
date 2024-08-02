package com.likelion.allForOne.domain.tblQuestion.tblAnswer.service;

import com.likelion.allForOne.domain.tblGroupMember.GroupMemberServiceImpl;
import com.likelion.allForOne.domain.tblQuestion.tblAnswer.TblAnswerRepository;
import com.likelion.allForOne.domain.tblQuestion.tblAnswer.dto.AnswerDto;
import com.likelion.allForOne.entity.TblAnswer;
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

    private final GroupMemberServiceImpl groupMemberService;

    /* =================================================================
     * Override
     * ================================================================= */
    /**
     * 답변자 기준 대상자별 답변 조회
     * @param usedQuestionSeq Long: 오늘의(출제된) 퀴즈 구분자
     * @param memberAnswerSeq Long: 답변자 멤버 구분자
     * @param memberTargetEntity TblGroupMember: 대상자 멤버 entity
     * @return AnswerDto.AnswerFormBasic
     */
    @Override
    public AnswerDto.BasedOnAnswerForm organizeBasedOnAnswerForm(Long usedQuestionSeq, Long memberAnswerSeq, TblGroupMember memberTargetEntity) {
        //1. 멤버 구분자
        Long memberTargetSeq = memberTargetEntity.getMemberSeq();

        //2. 답변 조회
        Optional<TblAnswer> bfAnswerOpt
                = answerRepository.findByUsedQuestion_UsedQuestionSeqAndMemberAnswer_MemberSeqAndMemberTarget_MemberSeq(
                        usedQuestionSeq, memberAnswerSeq, memberTargetSeq);

        //3. 데이터 반환
        return AnswerDto.BasedOnAnswerForm.builder()
                .memberTargetSeq(memberTargetSeq)
                .memberTargetName(groupMemberService.findMemberTargetName(memberTargetEntity))
                .answerTmpYn(bfAnswerOpt.map(TblAnswer::getAnswerTmpYn).orElse(2))
                .answerSeq(bfAnswerOpt.map(TblAnswer::getAnswerSeq).orElse(null))
                .answerContents(bfAnswerOpt.map(TblAnswer::getAnswerContents).orElse(null))
                .build();
    }

    /**
     * 대상자 기준 답변자별 답변 조회
     * @param usedQuestionSeq Long: 오늘의(출제된) 퀴즈 구분자
     * @param memberAnswerEntity TblGroupMember: 답변자 멤버 entity
     * @param memberTargetSeq Long: 대상자 멤버 구분자
     * @return AnswerDto.AnswerForm3
     */
    @Override
    public AnswerDto.BasedOnTargetForm organizeBasedOnTargetForm(Long usedQuestionSeq, TblGroupMember memberAnswerEntity, Long memberTargetSeq) {
        //1. Based 멤버 구분자
        Long memberAnswerSeq = memberAnswerEntity.getMemberSeq();

        //2. 답변 조회
        Optional<TblAnswer> bfAnswerOpt
                = answerRepository.findByUsedQuestion_UsedQuestionSeqAndMemberAnswer_MemberSeqAndMemberTarget_MemberSeq(
                        usedQuestionSeq, memberAnswerSeq, memberTargetSeq);

        //3. 데이터 반환
        return AnswerDto.BasedOnTargetForm.builder()
                .memberAnswerSeq(memberAnswerSeq)
                .memberAnswerName(groupMemberService.findMemberTargetName(memberAnswerEntity))
                .answerTmpYn(bfAnswerOpt.map(TblAnswer::getAnswerTmpYn).orElse(2))
                .answerSeq(bfAnswerOpt.map(TblAnswer::getAnswerSeq).orElse(null))
                .answerContents(bfAnswerOpt.map(TblAnswer::getAnswerContents).orElse(null))
                .build();
    }
}
