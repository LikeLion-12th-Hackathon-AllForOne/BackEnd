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
import java.util.Optional;

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
            System.out.println(todayQuestion[0] != null ? ((Number) todayQuestion[0]).longValue() : null);
            System.out.println(todayQuestion[1] != null ? ((Number) todayQuestion[1]).longValue() : null);


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
}
