package com.likelion.allForOne.tblGroup.dto;

import lombok.Data;

@Data
public class GroupRequestDto {
    @Data
    public static class saveOneGroup {
        private Long codeCategorySeq;   // 카테고리 코드 구분자
        private Integer groupMemberCnt; // 인원수
        private String groupName;       // 그룹명
    }
    @Data
    public static class saveGroupMemberByInviteCode {
        private String groupInviteCode;
    }
    @Data
    public static class findGroupDetail {
        private Long groupSeq;
    }
}
