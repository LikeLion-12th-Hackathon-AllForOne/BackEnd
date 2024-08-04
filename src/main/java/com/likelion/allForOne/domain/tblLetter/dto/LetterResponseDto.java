package com.likelion.allForOne.domain.tblLetter.dto;

import com.likelion.allForOne.domain.tblLetterPaper.LetterPaperResponseDto;
import com.likelion.allForOne.entity.TblLetterPaper;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LetterResponseDto {
    @Getter
    @Builder
    public static class searchLetterInfo{
        private String letterFrom;
        private String letterTo;
        private List<LetterPaperResponseDto.searchLetterPaperInfo> letterPaper;
    }
}
