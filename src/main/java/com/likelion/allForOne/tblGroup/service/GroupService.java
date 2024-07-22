package com.likelion.allForOne.tblGroup.service;

import com.likelion.allForOne.global.response.ApiResponse;
import com.likelion.allForOne.tblGroup.dto.GroupRequestDto;

public interface GroupService {
    ApiResponse<?> saveOneGroup(GroupRequestDto.saveOneGroup data, Long userSeq); //방(그룹) 생성
//    ApiResponse<?> findListJoinGroup(Long userSeq); //참여 방(그룹) 리스트
    ApiResponse<?> findGroupInviteCode(Long groupSeq, Long userSeq); //초대코드 조회
//    ApiResponse<?> saveGroupMemberByInviteCode(GroupRequestDto.saveGroupMemberByInviteCode data, Long userSeq); //초대코드 입장
//    ApiResponse<?> finsGroupDetail(GroupRequestDto.finsGroupDetail data, Long userSeq); //그룹 정보 상세 조회
}
