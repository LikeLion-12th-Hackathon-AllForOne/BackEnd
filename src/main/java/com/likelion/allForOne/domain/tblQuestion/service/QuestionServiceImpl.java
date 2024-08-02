package com.likelion.allForOne.domain.tblQuestion.service;


import com.likelion.allForOne.domain.tblCode.service.CodeServiceImpl;
import com.likelion.allForOne.domain.tblGroupMember.GroupMemberServiceImpl;
import com.likelion.allForOne.domain.tblQuestion.tblAnswer.dto.AnswerDto;
import com.likelion.allForOne.domain.tblQuestion.dto.QuestionDto;
import com.likelion.allForOne.domain.tblQuestion.dto.QuestionRequestDto;
import com.likelion.allForOne.domain.tblQuestion.dto.QuestionResponseDto;
import com.likelion.allForOne.domain.tblQuestion.tblAddQuestion.TblAddQuestionRepository;
import com.likelion.allForOne.domain.tblQuestion.tblAnswer.TblAnswerRepository;
import com.likelion.allForOne.domain.tblQuestion.tblAnswer.service.AnswerServiceImpl;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {
    private final TblAddQuestionRepository addQuestionRepository;
    private final TblComQuestionRepository comQuestionRepository;
    private final TblUsedQuestionRepository usedQuestionRepository;
    private final TblAnswerRepository answerRepository;

    private final CodeServiceImpl codeService;
    private final GroupMemberServiceImpl groupMemberService;
    private final AnswerServiceImpl answerService;

    /* =================================================================
     * Override
     * ================================================================= */
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
                    .group(groupEntity)
                    .build();
            usedQuestionRepository.save(usedQuestion);

            //5. 질문 생성 완료
            TblCode codeEntity = codeService.findCodeByCodeVal(1, "questionState", 1);
            groupEntity.updateQuestionState(codeEntity);
        }
    }

    /**
     * 오늘의 퀴즈 출제 상태 및 오늘의 퀴즈 구분자 조회
     * @param groupEntity TblGroup: 방(그룹) entity
     * @return Long: 오늘의 퀴즈 구분자
     */
    @Override
    public Long findTodayQuestionState(TblGroup groupEntity){
        //1. 오늘의 퀴즈 출제 상태 확인
        int todayQuestionState = groupEntity.getCodeQuestionStateSeq().getCodeVal();
        if (todayQuestionState != 1) return null;
        
        //2. 오늘의 퀴즈 조회
        Optional<TblUsedQuestion> usedQuestionOpt
                = usedQuestionRepository.findByInpDateAndGroup_GroupSeq(LocalDate.now(), groupEntity.getGroupSeq());
        return usedQuestionOpt.map(TblUsedQuestion::getUsedQuestionSeq).orElse(null);
    }

    /**
     * 오늘의(제출된) 질문 정리
     * @param usedQuestionSeq Long:오늘의(제출된) 질문 구분자
     * @return QuestionDto.OrganizeQuestion
     */
    @Override
    public QuestionDto.OrganizeQuestion organizeUsedQuestion(Long usedQuestionSeq) {
        //1. groupSeq 와 inpDate 가 오늘의 usedQuestion 데이터 조회
        Object[] todayQuestionOpt = usedQuestionRepository.findByUsedQuestionSeq(usedQuestionSeq);
        if (todayQuestionOpt.length == 0) return null;

        //2. 데이터 반환
        Object[] todayQuestion = (Object[]) todayQuestionOpt[0];
        return QuestionDto.OrganizeQuestion.builder()
                .questionType(((Number)todayQuestion[1]).intValue() == 1 ? 1 : 2) // 1: 전체 질문 / 2: 개별질문
                .usedQuestionSeq(((Number)todayQuestion[0]).longValue())
                .inpDate((String)todayQuestion[2])
                .question((String)todayQuestion[3]+todayQuestion[4])
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
     * 답변 (임시)저장 : 임시저장 여부(저장:0 / 임시저장:1)
     * @param data QuestionRequestDto.SaveAnswer: 입력받은 답변 데이터
     * @param userSeq Long: 로그인 사용자 구분자
     * @return ApiResponse<?>
     */
    @Override
    @Transactional
    public ApiResponse<?> saveAnswer(int answerTmpYn, QuestionRequestDto.SaveAnswerList data, Long userSeq) {
        //1. 멤버(답변생성자)와 로그인 사용자가 동일인이지 확인
        TblGroupMember memberAnswer = groupMemberService.findByGroupMemberSeq(data.getMemberAnswerSeq());
        if(memberAnswer == null
                || !userSeq.equals(memberAnswer.getUser().getUserSeq()))
            return ApiResponse.ERROR(ErrorCode.UNAUTHORIZED);

        //2. 질문조회 & 질문의 그룹과 멤버의 그룹이 같은지 확인
        Optional<TblUsedQuestion> questionOpt = usedQuestionRepository.findById(data.getQuestionSeq());
        if(questionOpt.isEmpty()
                || !questionOpt.get().getGroup().getGroupSeq().equals(memberAnswer.getGroup().getGroupSeq()))
            return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);


        //3. 질문 유형에 따라 답변 한개 또는 리스트 임시 저장
        int successCnt = 0;
        int saveLength = data.getSaveAnswerList().size();
        TblUsedQuestion question = questionOpt.get();
        if (question.getCodeQuestionType().getCodeSeq() == 28L) {
            //4. 회원수이상의 답변 입력은 불가능함.
            int possibleCnt = question.getGroup().getGroupMemberCnt()-1;
            if (saveLength > possibleCnt) return ApiResponse.ERROR(ErrorCode.INVALID_PARAMETER);
            //5. 답변 리스트 반영
            for(AnswerDto.SaveAnswer saveData : data.getSaveAnswerList())
                successCnt += answerReflect(answerTmpYn, memberAnswer, question, saveData);
        } else {
            //6. 답변 개수가 한개이고 입력받은 타겟멤버가 질문의 타겟멤버와 동일한지 확인
            AnswerDto.SaveAnswer saveData = data.getSaveAnswerList().get(0);
            if (saveLength > 1
                    || !saveData.getMemberTargetSeq().equals(question.getMemberTarget().getMemberSeq()))
                return ApiResponse.ERROR(ErrorCode.INVALID_PARAMETER);
            //7. 답변 한개 반영
            successCnt += answerReflect(answerTmpYn, memberAnswer, question, saveData);
        }

        //8. 결과 반환
        return successCnt == saveLength
                ? ApiResponse.SUCCESS(SuccessCode.SAVE_ANSWER)
                : ApiResponse.ERROR(ErrorCode.SAVE_SOME_FAIL);
    }

    /**
     * 오늘의 질문 답변(임시저장 or 저장) 조회
     * @param usedQuestionSeq Long:오늘의 질문 구분자
     * @param memberSeq Long: 멤버 구분자
     * @param userSeq Long: 로그인 사용자
     * @return ApiResponse<?>
     */
    @Override
    public ApiResponse<?> findTodayQandA(Long usedQuestionSeq, Long memberSeq, Long userSeq) {
        //1. 멤버(답변생성자)와 로그인 사용자가 동일인이지 확인
        TblGroupMember memberAnswer = groupMemberService.findByGroupMemberSeq(memberSeq);
        if(memberAnswer == null
                || !userSeq.equals(memberAnswer.getUser().getUserSeq()))
            return ApiResponse.ERROR(ErrorCode.UNAUTHORIZED);

        //2. 질문조회 & 질문의 그룹과 멤버의 그룹이 같은지 & 오늘자 질문이 맞는지 확인
        TblGroup groupEntity = memberAnswer.getGroup();
        Optional<TblUsedQuestion> todayQuestionOpt = usedQuestionRepository.findById(usedQuestionSeq);
        if(todayQuestionOpt.isEmpty()
                || !todayQuestionOpt.get().getGroup().getGroupSeq().equals(groupEntity.getGroupSeq())
                || !todayQuestionOpt.get().getInpDate().equals(LocalDate.now()))
            return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);

        //3. 방(그룹)에 속한 멤버 리스트
        List<TblGroupMember> groupMemberList = groupMemberService.findListGroupMemberByGroup(groupEntity.getGroupSeq());

        //4. 질문 정리
        TblUsedQuestion todayQuestion = todayQuestionOpt.get();
        QuestionDto.OrganizeQuestion question = organizeUsedQuestion(usedQuestionSeq);

        //5. 질문 유형에 따라 답변 한개 또는 리스트
        List<AnswerDto.BasedOnAnswerForm> answerFormList
                = answerService.findByBasedOnAnswer(todayQuestion, memberSeq, groupMemberList);

        //6. 데이터 반환
        return ApiResponse.SUCCESS(
                SuccessCode.FOUND_IT,
                new QuestionResponseDto.QuestionAndAnswer<>(question,answerFormList));
    }

    /**
     * 지난 오늘의 퀴즈 모아보기 (7개씩)
     * @param groupSeq Long: 방(그룹) 구분자
     * @param userSeq Long: 사용자 구분자
     * @return ApiResponse<?>
     */
    @Override
    public ApiResponse<?> findLastQandA(Long groupSeq, Long userSeq, String inpDate) {
        //1. 해당 유저가 그룹에 속한 유저인지 확인
        boolean memberCheck = groupMemberService.checkGroupMember(groupSeq, userSeq);
        if (!memberCheck) return ApiResponse.ERROR(ErrorCode.UNAUTHORIZED);

        //2. 그룹에 속한 유저(멤버) 리스트 조회
        List<TblGroupMember> groupMemberList = groupMemberService.findListGroupMemberByGroup(groupSeq);

        //3. 미래의 데이터를 조회할수는 없음.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(inpDate, formatter);
        if(date.isAfter(LocalDate.now())) return ApiResponse.ERROR(ErrorCode.INVALID_PARAMETER);

        //4. inpDate 를 포함한 이후 6일간의 오늘의(제출된) 퀴즈 리스트, 총 7개 조회
        List<TblUsedQuestion> usedQuestionList
                = usedQuestionRepository.findTop7ByInpDateLessThanEqualAndGroup_GroupSeqOrderByInpDateDesc(date, groupSeq);

        //5. 각 퀴즈별 멤버들의 답변 조회
        List<QuestionResponseDto.QuestionAndAnswer<?>> result = new ArrayList<>();
        for(TblUsedQuestion usedQuestionEntity : usedQuestionList)
            result.add(organizeQuestionAndAnswerAll(usedQuestionEntity, groupMemberList));

        return ApiResponse.SUCCESS(SuccessCode.FOUND_IT, result);
    }

    /**
     * 특정 그룹에서 특정인에 대한 퀴즈 모아보기
     * @param memberTargetSeq Long: 특정인(멤버) 구분자
     * @param userSeq Long: 로그인 사용자 구분자
     * @return ApiResponse<?>
     */
    @Override
    public ApiResponse<?> findSomeoneQAndA(Long memberTargetSeq, Long userSeq, String inpDate) {
        //1. 멤버 구분자에 대한 조회
        TblGroupMember memberTarget = groupMemberService.findByGroupMemberSeq(memberTargetSeq);
        if(memberTarget == null) return ApiResponse.ERROR(ErrorCode.RESOURCE_NOT_FOUND);
        Long groupSeq = memberTarget.getGroup().getGroupSeq();  //질문 대상의 방(그룹) 구분자

        //2. 멤버 구분자가 속한 그룹에 로그인 사용자가 속했는지 확인하기
        boolean userCheck = groupMemberService.checkGroupMember(groupSeq, userSeq);
        if(!userCheck) return ApiResponse.ERROR(ErrorCode.UNAUTHORIZED);

        //3. 미래의 데이터를 조회할수는 없음.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(inpDate, formatter);
        if(date.isAfter(LocalDate.now())) return ApiResponse.ERROR(ErrorCode.INVALID_PARAMETER);

        //4. 전체 질문과 멤버 구분자를 target 으로 하는 질문 조회하기
        List<TblUsedQuestion> targetQuestionList
                = usedQuestionRepository.findSomeoneQAndA(date, groupSeq, memberTargetSeq);

        //5. 그룹에 속한 멤버 조회
        List<TblGroupMember> groupMemberList = groupMemberService.findListGroupMemberByGroup(groupSeq);

        //6. 각 질문에 대한 멤버 구분자를 target 으로 하는 답변 조회하기
        List<QuestionResponseDto.QuestionAndAnswer<?>> result = new ArrayList<>();
        for (TblUsedQuestion usedQuestionEntity : targetQuestionList) {
            //7. 질문 정리하기
            Long usedQuestionSeq = usedQuestionEntity.getUsedQuestionSeq();
            QuestionDto.OrganizeQuestion organizeUsedQuestion = organizeUsedQuestion(usedQuestionSeq);

            //8. memberTarget 에 대한 멤버 전부의 답변 취합하기
            List<AnswerDto.BasedOnTargetForm> answerFormList
                    = answerService.findByBasedOnTarget(
                            usedQuestionEntity.getCodeQuestionType().getCodeSeq() == 28L,
                            usedQuestionSeq, groupMemberList, memberTargetSeq);
            result.add(new QuestionResponseDto.QuestionAndAnswer<>(organizeUsedQuestion, answerFormList));
        }

        //7. 결과 반환
        return ApiResponse.SUCCESS(SuccessCode.FOUND_IT, result);
    }

    /* =================================================================
     * 분리 코드
     * ================================================================= */
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

    /**
     * 질문 저장
     * @param answerTmpYn int: 임시저장 여부(저장:0 / 임시저장:1)
     * @param memberAnswer TblGroupMember: 답변하는 멤버 entity
     * @param question TblUsedQuestion: 질문 entity
     * @param saveData AnswerDto.SaveAnswer: 반영해야하는 답변
     * @return int 성공여부(0:실패 / 1:성공)
     */
    private int answerReflect(int answerTmpYn, TblGroupMember memberAnswer, TblUsedQuestion question, AnswerDto.SaveAnswer saveData){
        //1. 질문 대상자 유효성 검사
        TblGroupMember memberTarget = groupMemberService.findByGroupMemberSeq(saveData.getMemberTargetSeq());
        if(memberTarget == null
                || !memberAnswer.getGroup().getGroupSeq()
                .equals(memberTarget.getGroup().getGroupSeq())) return 0;

        //2. 이전에 (임시)저장한 기록이 있는지 확인
        Optional<TblAnswer> bfAnswerOpt
                = answerRepository.findByUsedQuestion_UsedQuestionSeqAndMemberAnswer_MemberSeqAndMemberTarget_MemberSeq(
                question.getUsedQuestionSeq(), memberAnswer.getMemberSeq(), memberTarget.getMemberSeq());   //질문, 답변자, 질문 대상자, 데이터로 기존에 있던 답변 데이터 확인
        if(bfAnswerOpt.isPresent()) {
            //3. 저장된 기록과 일치하지 않는 정보에 대해 수정/저장을 진행하지 않음
            TblAnswer bfAnswer = bfAnswerOpt.get();
            if(!bfAnswer.getAnswerSeq().equals(saveData.getAnswerSeq())) return 0;

            //4. 2에서 기록이 있을 경우, update
            bfAnswer.updateAnswerContents(saveData.getAnswerContents(), answerTmpYn);
            return 1;
        }

        //5. 2에서 기록이 없을 경우, save
        TblAnswer answer = TblAnswer.builder()
                .answerContents(saveData.getAnswerContents())
                .answerTmpYn(answerTmpYn)
                .usedQuestion(question)
                .memberAnswer(memberAnswer)
                .memberTarget(memberTarget)
                .build();
        answerRepository.save(answer);
        return 1;
    }

    /**
     * 질문정리 및 해당 질문에 대한 멤버 리스트의 답변 모아보기 정리
     * @param usedQuestionEntity TblUsedQuestion: 오늘의(제출된) 퀴즈 entity
     * @param groupMemberList List<TblGroupMember>: 방(그룹)에 참가하고 있는 멤버 리스트
     * @return QuestionResponseDto.QuestionAndAnswer: 질문과 답변리스트가 정리된 DTO
     */
    private QuestionResponseDto.QuestionAndAnswer<?> organizeQuestionAndAnswerAll(TblUsedQuestion usedQuestionEntity, List<TblGroupMember> groupMemberList){
        //1. 질문 정리
        Long usedQuestionSeq = usedQuestionEntity.getUsedQuestionSeq();
        QuestionDto.OrganizeQuestion organizeUsedQuestion = organizeUsedQuestion(usedQuestionSeq);
        //2. 멤버별 답변 조회
        List<AnswerDto.BasedOnAnswerFormList> memberAnswerList = new ArrayList<>();
        for(TblGroupMember groupAnswerMember : groupMemberList){
            Long groupAnswerMemberSeq = groupAnswerMember.getMemberSeq();
            List<AnswerDto.BasedOnAnswerForm> answerFormList
                    = answerService.findByBasedOnAnswer(usedQuestionEntity, groupAnswerMemberSeq, groupMemberList);
            //6. 멤버별 답변 DTO 정리
            memberAnswerList.add(AnswerDto.BasedOnAnswerFormList.builder()
                    .memberAnswerSeq(groupAnswerMemberSeq)
                    .memberAnswerName(groupMemberService.findMemberTargetName(groupAnswerMember))
                    .memberAnswerList(answerFormList)
                    .build());
        }
        //7. 데이터 반환
        return new QuestionResponseDto.QuestionAndAnswer<>(organizeUsedQuestion, memberAnswerList);
    }

}
