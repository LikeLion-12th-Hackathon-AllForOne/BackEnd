package com.likelion.allForOne.domain.tblUser.dto;

import com.likelion.allForOne.entity.TblUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserResponseDto {
    private Long userSeq;
    private String userId;
    private String userPwd;
    private String userName;
    private String userBirth;
    private String userPhone;
    private String userImg;
    private String codeMbti;

    public static UserResponseDto UserJoinResponseDto(TblUser user) {
        return UserResponseDto.builder()
                .userSeq(user.getUserSeq())
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userBirth(user.getUserBirth())
                .userPhone(user.getUserPhone())
                .userImg(user.getUserImg())
                .codeMbti(user.getCodeMbti().getCodeName())
                .build();
    }
}
