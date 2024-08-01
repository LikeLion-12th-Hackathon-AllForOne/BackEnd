package com.likelion.allForOne.domain.tblCode.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CodeResponseDto {
    @Getter
    public static class findListUnit{
        private List<CodeDto.simple1> list;
        public findListUnit(List<CodeDto.simple1> list){
            this.list = list;
        }
    }
}
