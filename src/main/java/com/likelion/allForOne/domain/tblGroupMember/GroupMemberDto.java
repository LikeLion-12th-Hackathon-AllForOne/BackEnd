package com.likelion.allForOne.domain.tblGroupMember;

import com.likelion.allForOne.entity.TblGroupMember;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupMemberDto {
    @Getter
    @Builder
    public static class profile {
        private Long memberSeq;      //그룹회원 구분자
        private String userName;    //이름
        private String userBirth;   //생일
        private String userPhone;   //전화번호
        private String codeName;    //MBTI
    }
}
