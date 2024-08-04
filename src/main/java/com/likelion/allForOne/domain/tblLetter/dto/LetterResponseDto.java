package com.likelion.allForOne.domain.tblLetter.dto;

import com.likelion.allForOne.domain.tblLetterPaper.LetterPaperResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LetterResponseDto {
    @Getter
    @Builder
    public static class searchLetterInfo {
        private String letterFrom;
        private String letterTo;
        private List<LetterPaperResponseDto.searchLetterPaperInfo> letterPaper;
    }

    @Getter
    @Builder
    public static class searchLetterList {
        private List<LetterRequestDto.searchLetterList> letterList;
    }
}
