package com.likelion.allForOne.domain.tblUser.dto;

import lombok.*;

public class UserRequestDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JoinDto {
        private Long userSeq;
        private String userId;
        private String userPwd;
        private String userName;
        private String userBirth;
        private String userPhone;
        private String userImg;
        private String codeMbti;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CheckIdDuplicateDto {
        private String userId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginDto {
        private String userId;
        private String userPwd;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CheckPwdDto {
        private String userPwd;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateUserInfo {
        private String userPwd;
        private String userName;
        private String userBirth;
        private String userPhone;
        private String userImg;
        private String codeMbti;
    }
}

