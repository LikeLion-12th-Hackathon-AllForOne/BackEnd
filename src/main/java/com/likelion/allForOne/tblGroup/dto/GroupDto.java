package com.likelion.allForOne.tblGroup.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupDto {
    @Getter
    @Builder
    public static class participateInfo {
        private Long memberSeq;         //그룹멤버구분자
        private String groupName;       //그룹명(방이름)
        private String categoryName;    //카테고리명
        private String ownerName;       //방장 이름
        private boolean ownerYn;        //방장 여부
        private boolean fullPackageYn;  //선물보따리 터짐 확인 여부
    }
}
