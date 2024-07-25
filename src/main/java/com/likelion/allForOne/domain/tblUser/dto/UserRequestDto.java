package com.likelion.allForOne.domain.tblUser.dto;

import lombok.*;

public class UserRequestDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserJoinRequestDto {
        private Long userSeq;
        private String userId;
        private String userPwd;
        private String userName;
        private String userBirth;
        private String userPhone;
        private String userImg;
        private String codeMbti;
    }
}

