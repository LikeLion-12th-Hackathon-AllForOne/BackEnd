package com.likelion.allForOne.domain.tblLetterPaper;

import com.likelion.allForOne.entity.TblLetterPaper;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class LetterPaperResponseDto {
    @Getter
    @Builder
    public static class searchLetterPaperInfo{
        private Long paperSeq;
        private String paperFileName;
    }
}
