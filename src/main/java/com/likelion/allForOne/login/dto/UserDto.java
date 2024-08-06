package com.likelion.allForOne.login.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDto {
    private Long userSeq;   // 구분자
    private String userId;      //아이디
    private String userName;    //이름
    private String userImg;     //개인 프로필 이미지 파일명
    private LocalDateTime inpDate; // 생성일
    private LocalDateTime modDate; // 수정일
}
