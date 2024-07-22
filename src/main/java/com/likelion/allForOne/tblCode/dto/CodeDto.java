package com.likelion.allForOne.tblCode.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CodeDto {
    @Getter
    @Builder
    public static class simple1{
        private Long codeSeq;       //구분자
        private String codeName;    //코드명
    }
}
