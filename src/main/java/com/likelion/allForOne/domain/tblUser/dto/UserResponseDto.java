package com.likelion.allForOne.domain.tblUser.dto;

import com.likelion.allForOne.entity.TblUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserResponseDto {
    @Getter
    @Builder
    public static class searchUserInfo{
        private Long userSeq;
        private String userId;
        private String userName;
        private String userBirth;
        private String userPhone;
        private String userImg;
        private String codeMbti;
    }
}
