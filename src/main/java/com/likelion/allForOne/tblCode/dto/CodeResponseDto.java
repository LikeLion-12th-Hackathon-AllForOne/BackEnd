package com.likelion.allForOne.tblCode.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CodeResponseDto {
    @Getter
    public static class findListUnit2{
        private List<CodeDto.simple1> list;
        public findListUnit2(List<CodeDto.simple1> list){
            this.list = list;
        }
    }
}
