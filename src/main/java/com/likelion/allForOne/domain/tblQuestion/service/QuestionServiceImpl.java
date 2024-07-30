package com.likelion.allForOne.domain.tblQuestion.service;


import com.likelion.allForOne.domain.tblCode.service.CodeServiceImpl;
import com.likelion.allForOne.domain.tblQuestion.dto.QuestionDto;
import com.likelion.allForOne.domain.tblQuestion.tblAddQuestion.TblAddQuestionRepository;
import com.likelion.allForOne.domain.tblQuestion.tblComQuestion.TblComQuestionRepository;
import com.likelion.allForOne.domain.tblQuestion.tblUsedQuestion.TblUsedQuestionRepository;
import com.likelion.allForOne.entity.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {
    private final TblAddQuestionRepository addQuestionRepository;
    private final TblComQuestionRepository comQuestionRepository;
    private final TblUsedQuestionRepository usedQuestionRepository;

    private final CodeServiceImpl codeService;

    /**
     * 오늘의 질문 생성하기
     * @param groupEntity TblGroup: 방(그룹) 엔티티
     */
    @Override
    public void createTodayQuestion(LocalDate inpDate, TblGroup groupEntity) {
        //1. 사용되지 않은 추가질문 랜덤으로 한개 조회하기
        Object[] result = addQuestionRepository.findByGroup(groupEntity.getGroupSeq());

        //2. 1에서 조회된 데이터가 없을 경우, 공통질문에서 카테고리에 맞춰서 랜덤으로 한개 조회
        if (result.length == 0)
            result = comQuestionRepository.findByGroup(groupEntity.getGroupSeq());

        if (result.length == 0){
            //3. 1&2 모두 조회된 데이터가 없는 경우, 질문 생성 실패
            TblCode codeEntity = codeService.findCodeByCodeVal(1, "questionState", 2);
            groupEntity.updateQuestionState(codeEntity);
        } else {
            //4. questionOpt 데이터로 오늘의 질문 생성
            Object[] todayQuestion = (Object[]) result[0];
            TblUsedQuestion usedQuestion = TblUsedQuestion.builder()
                    .addQuestionSeq(todayQuestion[0] == null ? null : TblAddQuestion.builder().addQuestionSeq(((Number)todayQuestion[0]).longValue()).build())
                    .comQuestionSeq(todayQuestion[1] == null ? null : TblComQuestion.builder().comQuestionSeq(((Number)todayQuestion[1]).longValue()).build())
                    .codeQuestionType(TblCode.builder().codeSeq(((Number)todayQuestion[2]).longValue()).build())
                    .codeQuestionClass(TblCode.builder().codeSeq(((Number)todayQuestion[3]).longValue()).build())
                    .memberTarget(todayQuestion[4] == null ? null : TblGroupMember.builder().memberSeq(((Number)todayQuestion[4]).longValue()).build())
                    .inpDate(inpDate)
                    .groupSeq(groupEntity)
                    .build();
            usedQuestionRepository.save(usedQuestion);

            //5. 질문 생성 완료
            TblCode codeEntity = codeService.findCodeByCodeVal(1, "questionState", 1);
            groupEntity.updateQuestionState(codeEntity);
        }
    }

    /**
     * 오늘의 퀴즈 조회
     * @param groupEntity Long:방(그룹) 엔티티
     * @return QuestionDto.todayQuestion
     */
    @Override
    public QuestionDto.todayQuestion findTodayQuestion(TblGroup groupEntity) {
        //1. 오늘의 퀴즈 상태에 따른 반환
        int todayQuestionState = groupEntity.getCodeQuestionStateSeq().getCodeVal();
        if (groupEntity.getCodeQuestionStateSeq().getCodeVal() != 1)
            return QuestionDto.todayQuestion.builder()
                    .questionStateVal(todayQuestionState)
                    .questionStateMsg(groupEntity.getCodeQuestionStateSeq().getCodeName())
                    .build();

        //2. groupSeq 와 inpDate 가 오늘의 usedQuestion 데이터 조회
        Object[] todayQuestionOpt = usedQuestionRepository.findByInpDateAndGroup_GroupSeq(groupEntity.getGroupSeq());
        if (todayQuestionOpt.length == 0)
            return QuestionDto.todayQuestion.builder()
                    .questionStateVal(4)
                    .questionStateMsg("문제출제를 기다리는 중입니다.")
                    .build();

        //3. 데이터 반환
        Object[] todayQuestion = (Object[]) todayQuestionOpt[0];
        return QuestionDto.todayQuestion.builder()
                .questionStateVal(todayQuestionState)
                .questionStateMsg(groupEntity.getCodeQuestionStateSeq().getCodeName())
                .questionType(((Number)todayQuestion[1]).intValue() == 1 ? 1 : 2) // 1: 전체 질문 / 2: 개별질문
                .usedQuestionSeq(((Number)todayQuestion[0]).longValue())
                .question((String)todayQuestion[2]+todayQuestion[3])
                .build();
    }


















}
