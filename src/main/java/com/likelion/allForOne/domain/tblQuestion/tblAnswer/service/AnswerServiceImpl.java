package com.likelion.allForOne.domain.tblQuestion.tblAnswer.service;

import com.likelion.allForOne.domain.tblGroupMember.GroupMemberServiceImpl;
import com.likelion.allForOne.domain.tblQuestion.tblAnswer.TblAnswerRepository;
import com.likelion.allForOne.domain.tblQuestion.tblAnswer.dto.AnswerDto;
import com.likelion.allForOne.entity.TblAnswer;
import com.likelion.allForOne.entity.TblGroupMember;
import com.likelion.allForOne.entity.TblUsedQuestion;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
     * 답변자 기준, 대상자별 답변 조회
     * @param usedQuestionEntity TblUsedQuestion: 제출된 퀴즈의 entity
     * @param memberAnswerSeq Long: 답변자 멤버 구분자
     * @param memberTargetList List<TblGroupMember>: 해당 그룹에 속한 회원 리스트(대상자)
     * @return List<AnswerDto.BasedOnAnswerForm> 답변자 기준, 대상자별 답변 리스트
     */
    @Override
    public List<AnswerDto.BasedOnAnswerForm> findByBasedOnAnswer(TblUsedQuestion usedQuestionEntity, Long memberAnswerSeq, List<TblGroupMember> memberTargetList){
        //1. 반환 리스트 객체 생성
        List<AnswerDto.BasedOnAnswerForm> answerFormList  = new ArrayList<>();
        //2. 회원별 답변 조회
        Long usedQuestionSeq = usedQuestionEntity.getUsedQuestionSeq();
        if (usedQuestionEntity.getCodeQuestionType().getCodeSeq() != 28L) //3. 개인 질문의 경우, 답변은 한개만 달린다.
            answerFormList.add(organizeBasedOnAnswerForm(usedQuestionSeq, memberAnswerSeq, usedQuestionEntity.getMemberTarget()));
        else {
            for(TblGroupMember memberTarget : memberTargetList){
                //4. 전체질문의 경우, 자신을 제외한 나머지에 대해 답변을 달아야함.
                if(memberTarget.getMemberSeq().equals(memberAnswerSeq)) continue;
                answerFormList.add(organizeBasedOnAnswerForm(usedQuestionSeq, memberAnswerSeq, memberTarget));
            }
        }
        //5. 데이터 반환
        return answerFormList;
    }

    /**
     * 대상자 기준, 답변자별 답변 조회
     * @param isAllQuestion 질문 구분 (전체 질문:true / 개인 질문:false)
     * @param usedQuestionSeq Long: 제출된 퀴즈의 구분자
     * @param memberAnswerList List<TblGroupMember>: 해당 그룹에 속한 회원 리스트(답변자)
     * @param memberTargetSeq Long: 대상자 멤버 구분자
     * @return List<AnswerDto.BasedOnTargetForm> 대상자 기준, 답변자별 답변 리스트
     */
    @Override
    public List<AnswerDto.BasedOnTargetForm> findByBasedOnTarget(boolean isAllQuestion, Long usedQuestionSeq, List<TblGroupMember> memberAnswerList, Long memberTargetSeq){
        return isAllQuestion
                ? organizeBasedOnAllTargetForm(usedQuestionSeq, memberAnswerList, memberTargetSeq)
                : organizeBasedOnOneTargetForm(usedQuestionSeq, memberAnswerList, memberTargetSeq);
    }

    /* =================================================================
     * 분리 코드
     * ================================================================= */
    /**
     * 답변자 기준 대상자별 답변 정리
     * @param usedQuestionSeq Long: 오늘의(출제된) 퀴즈 구분자
     * @param memberAnswerSeq Long: 답변자 멤버 구분자
     * @param memberTargetEntity TblGroupMember: 대상자 멤버 entity
     * @return AnswerDto.AnswerFormBasic
     */
    private AnswerDto.BasedOnAnswerForm organizeBasedOnAnswerForm(Long usedQuestionSeq, Long memberAnswerSeq, TblGroupMember memberTargetEntity) {
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
     * 전체 질문에 대한, 대상자 기준, 답변자별 답변 조회
     * @param usedQuestionSeq Long: 제출된 퀴즈의 구분자
     * @param memberAnswerList List<TblGroupMember>: 해당 그룹에 속한 회원 리스트(답변자)
     * @param memberTargetSeq Long: 대상자 멤버 구분자
     * @return List<AnswerDto.BasedOnTargetForm> 대상자 기준, 답변자별 답변 리스트
     */
    private List<AnswerDto.BasedOnTargetForm> organizeBasedOnAllTargetForm(Long usedQuestionSeq, List<TblGroupMember> memberAnswerList, Long memberTargetSeq){
        //1. 반환 리스트 객체 생성
        List<AnswerDto.BasedOnTargetForm> answerFormList = new ArrayList<>();
        //2. 회원별 답변 조회
        for(TblGroupMember memberAnswer : memberAnswerList){
            //3. 전체 질문에는 본인 답변이 존재하지 않음
            if(memberAnswer.getMemberSeq().equals(memberTargetSeq)) continue;
            //4. 답변 조회
            answerFormList.add(organizeBasedOnTargetForm(usedQuestionSeq, memberAnswer, memberTargetSeq));
        }
        //5. 데이터 반환
        return answerFormList;
    }

    /**
     * 개인 질문에 대한, 대상자 기준, 답변자별 답변 조회
     * @param usedQuestionSeq Long: 제출된 퀴즈의 구분자
     * @param memberAnswerList List<TblGroupMember>: 해당 그룹에 속한 회원 리스트(답변자)
     * @param memberTargetSeq Long: 대상자 멤버 구분자
     * @return List<AnswerDto.BasedOnTargetForm> 대상자 기준, 답변자별 답변 리스트
     */
    private List<AnswerDto.BasedOnTargetForm> organizeBasedOnOneTargetForm(Long usedQuestionSeq, List<TblGroupMember> memberAnswerList, Long memberTargetSeq){
        //1. 반환 리스트 객체 생성
        List<AnswerDto.BasedOnTargetForm> answerFormList = new ArrayList<>();
        //2. 회원별 답변 조회
        for(TblGroupMember memberAnswer : memberAnswerList){
            AnswerDto.BasedOnTargetForm basedOnTargetForm = organizeBasedOnTargetForm(usedQuestionSeq, memberAnswer, memberTargetSeq);
            //3. 본인이 한 답변은 항상 맨 처음에 위치해야 한다.
            if(memberAnswer.getMemberSeq().equals(memberTargetSeq))
                answerFormList.add(0, basedOnTargetForm);
            else answerFormList.add(basedOnTargetForm);
        }
        //3. 데이터 반환
        return answerFormList;
    }

    /**
     * 대상자 기준 답변자별 답변 정리
     * @param usedQuestionSeq Long: 오늘의(출제된) 퀴즈 구분자
     * @param memberAnswerEntity TblGroupMember: 답변자 멤버 entity
     * @param memberTargetSeq Long: 대상자 멤버 구분자
     * @return AnswerDto.AnswerForm3
     */
    private AnswerDto.BasedOnTargetForm organizeBasedOnTargetForm(Long usedQuestionSeq, TblGroupMember memberAnswerEntity, Long memberTargetSeq) {
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
