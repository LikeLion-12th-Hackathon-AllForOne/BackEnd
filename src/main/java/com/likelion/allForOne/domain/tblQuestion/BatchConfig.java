package com.likelion.allForOne.domain.tblQuestion;

import com.likelion.allForOne.domain.tblGroup.service.GroupServiceImpl;
import com.likelion.allForOne.domain.tblQuestion.service.QuestionServiceImpl;
import com.likelion.allForOne.entity.TblGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchConfig {
    private final GroupServiceImpl groupService;
    private final QuestionServiceImpl questionService;

    /**
     * 매일 자정에 오늘의 문제 생성하는 배치 코드
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") //(초 분 시 일 월 요일)
    public void createTodayQuestion(){
        System.out.println("스케쥴링 잘 먹는지 확인만 하장~");
        //1. 전체 그룹 조회
        List<TblGroup> tblGroupList = groupService.findListAllGroup();
        for(TblGroup groupEntity : tblGroupList){
            //2. 질문 생성하기
            questionService.createTodayQuestion(groupEntity);
        }
    }
}
