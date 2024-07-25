package com.likelion.allForOne.tblGroup.dto;

import com.likelion.allForOne.entity.TblGroupMember;
import com.likelion.allForOne.tblGroupMember.GroupMemberDto;
import com.likelion.allForOne.tblQuestion.QuestionDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class GroupResponseDto {
    @Getter
    @Builder
    public static class findListJoinGroup{
        private String userImg;                       //개인 프로필 이미지 파일명
        private String userName;                      //이름
        private List<GroupDto.participateInfo> list;  //참여그룹 리스트
    }
    @Getter
    @Builder
    public static class findGroupDetail{
        private boolean ownerYn;                                //유저의 방장 여부
        private String groupName;                               //방(그룹)이름
        private long dayAfterCnt;                               //생성일로부터 며칠
        private int achievePercent;                             //보따리 달성 퍼센트
        private QuestionDto.todayQuiz todayQuiz;                //오늘의 퀴즈
        private List<GroupMemberDto.profile> groupMemberList;   //그룹멤버 프로필 리스트

        public void setMemberList(List<TblGroupMember> groupMemberList) {
            for(TblGroupMember entity : groupMemberList){


            }
        }
    }
}
