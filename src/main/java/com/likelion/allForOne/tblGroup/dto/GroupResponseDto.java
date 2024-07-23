package com.likelion.allForOne.tblGroup.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class GroupResponseDto {
    @Getter
    @Builder
    public static class findListJoinGroup{
        private String userImg;                             //개인 프로필 이미지 파일명
        private String userName;                            //이름
        private List<GroupDto.participateInfo> list;  //참여그룹 리스트
    }
}
