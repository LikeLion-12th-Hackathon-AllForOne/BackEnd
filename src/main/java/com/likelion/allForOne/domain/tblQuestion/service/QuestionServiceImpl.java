package com.likelion.allForOne.domain.tblQuestion.service;


import com.likelion.allForOne.domain.tblCode.service.CodeServiceImpl;
import com.likelion.allForOne.domain.tblGroupMember.GroupMemberServiceImpl;
import com.likelion.allForOne.domain.tblQuestion.dto.QuestionDto;
import com.likelion.allForOne.domain.tblQuestion.dto.QuestionRequestDto;
import com.likelion.allForOne.domain.tblQuestion.tblAddQuestion.TblAddQuestionRepository;
import com.likelion.allForOne.domain.tblQuestion.tblComQuestion.TblComQuestionRepository;
import com.likelion.allForOne.domain.tblQuestion.tblUsedQuestion.TblUsedQuestionRepository;
import com.likelion.allForOne.entity.*;
import com.likelion.allForOne.global.response.ApiResponse;
import com.likelion.allForOne.global.response.resEnum.ErrorCode;
import com.likelion.allForOne.global.response.resEnum.SuccessCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {
    private final TblAddQuestionRepository addQuestionRepository;
    private final TblComQuestionRepository comQuestionRepository;
    private final TblUsedQuestionRepository usedQuestionRepository;

    private final CodeServiceImpl codeService;
    private final GroupMemberServiceImpl groupMemberService;

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

    /**
     * 질문 추가하기
     * @param data QuestionRequestDto.AddQuestion: 사용자가 추가한 질문 데이터
     * @param userSeq Long: 로그인 사용자 구분자
     * @return ApiResponse<?>
     */
    @Override
    @Transactional
    public ApiResponse<?> addQuestion(QuestionRequestDto.AddQuestion data, Long userSeq) {
        //1. 멤버 조회 및 로그인 사용자와 동일인이지 확인
        TblGroupMember member = groupMemberService.findByGroupMemberSeq(data.getMemberSeq());
        if(member == null || !userSeq.equals(member.getUser().getUserSeq()))
            return ApiResponse.ERROR(ErrorCode.UNAUTHORIZED);

        //2. 질문구분 코드 구분자(codeQuestionClass) 찾기 (멤버의 카테고리 / 가족 카테고리의 경우, 생성자의 역할에 따라)
        Long codeQuestionClassSeq = findCodeQuestionClassByRole(member.getCodeCategoryRole().getCodeSeq());

        //3. entity 생성 (질문 생성자는 멤버 구분자)
        TblAddQuestion entity = TblAddQuestion.builder()
                .addQuestion(data.getAddQuestion())
                .codeQuestionClass(TblCode.builder().codeSeq(codeQuestionClassSeq).build())
                .memberCreate(member)
                .build();
        addQuestionRepository.save(entity);

        //4. 결과 반환
        return ApiResponse.SUCCESS(SuccessCode.CREATE_QUESTION);
    }

    /**
     * 멤버 역할 값으로 질문구분 코드 구분자(codeQuestionClass) 찾기
     * @param codeCategoryRoleSeq Long: 멤버 역할
     * @return Long: 질문구분 코드 구분자(codeQuestionClass)
     */
    private Long findCodeQuestionClassByRole(Long codeCategoryRoleSeq){
        if (codeCategoryRoleSeq == 38L || codeCategoryRoleSeq == 39L) return 32L;
        else if (codeCategoryRoleSeq == 40L) return 33L;
        else if (codeCategoryRoleSeq == 41L) return 34L;
        else if (codeCategoryRoleSeq == 42L) return 35L;
        else return null;
    }


}
