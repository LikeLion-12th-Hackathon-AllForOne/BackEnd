package com.likelion.allForOne.domain.tblQuestion.service;


import com.likelion.allForOne.domain.tblQuestion.tblAddQuestion.TblAddQuestionRepository;
import com.likelion.allForOne.domain.tblQuestion.tblComQuestion.TblComQuestionRepository;
import com.likelion.allForOne.domain.tblQuestion.tblUsedQuestion.TblUsedQuestionRepository;
import com.likelion.allForOne.entity.TblAddQuestion;
import com.likelion.allForOne.entity.TblGroup;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final TblAddQuestionRepository addQuestionRepository;
    private final TblComQuestionRepository comQuestionRepository;
    private final TblUsedQuestionRepository usedQuestionRepository;

    /**
     * 오늘의 질문 생성하기
     * @param entity TblGroup: 방(그룹) 엔티티
     */
    @Override
    public void createTodayQuestion(TblGroup entity) {
        //1. 사용되지 않은 추가질문 랜덤으로 한개 조회하기
        Optional<TblAddQuestion> addQuestionOpt = addQuestionRepository.findByGroup(entity.getGroupSeq());
        //2. 1에서 조회된 데이터가 있을 경우, 해당 데이터로 오늘의 질문 생성
        //3. 1에서 조회된 데이터가 없을 경우, 공통질문에서 카테고리에 맞춰서 랜덤으로 한개 조회
        //4. 3에서 조회한 데이터가 있을 경우, 해당데이터로 오늘의 질문 생성
        //5. 3에서 조회한 데이터가 없을 경우 return
    }
}
