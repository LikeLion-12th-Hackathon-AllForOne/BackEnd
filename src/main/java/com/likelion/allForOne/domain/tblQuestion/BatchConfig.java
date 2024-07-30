package com.likelion.allForOne.domain.tblQuestion;

import com.likelion.allForOne.domain.tblGroup.service.GroupServiceImpl;
import com.likelion.allForOne.domain.tblGroupMember.GroupMemberServiceImpl;
import com.likelion.allForOne.domain.tblQuestion.service.QuestionServiceImpl;
import com.likelion.allForOne.entity.TblGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchConfig {
    private final GroupServiceImpl groupService;
    private final GroupMemberServiceImpl groupMemberService;
    private final QuestionServiceImpl questionService;

    /**
     * 매일 자정에 오늘의 문제 생성하는 배치 코드
     */
    @Transactional
//    @Scheduled(cron = "30 3 0 * * *") //(초 분 시 일 월 요일)
    @Scheduled(cron = "0 0 0 * * *") //(초 분 시 일 월 요일)
    public void createTodayQuestion(){
        //1. 문제 생성일 조회
        LocalDate inpDate = LocalDate.now();
        //2. 인원수 전부 채워진 그룹에 한해서 전체 그룹 조회 (수정필요)
        List<TblGroup> tblGroupList = groupService.findListAllGroup();
        for(TblGroup groupEntity : tblGroupList){
            //3. 인원수 확인하기 (초대 인원이 전부 채워지지 않으면 문제를 출제하지 않음.)
            long cntJoinMember = groupMemberService.cntJoinGroupMember(groupEntity.getGroupSeq());
            if (groupEntity.getGroupMemberCnt() > cntJoinMember) continue;

            //4. 질문 생성하기
            questionService.createTodayQuestion(inpDate, groupEntity);
        }
    }
}
