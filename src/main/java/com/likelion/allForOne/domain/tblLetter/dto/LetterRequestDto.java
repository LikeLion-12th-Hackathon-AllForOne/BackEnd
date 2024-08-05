package com.likelion.allForOne.domain.tblLetter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LetterRequestDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateLetterDto {
        private Long letter_seq;         // 구분자
        private String letter_to;        // 누구에게
        private String letter_from;      // 누구로부터
        private String letter_contents;  // 편지 내용
        private String letter_read;      // 편지 읽음 여부
        private String member_to;        // 받는 사람 구분자
        private String member_from;      // 보낸 사람 구분자
        private String paper_seq;        // 편지지 구분자
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchLetterInfo {
        private String letter_to;        // 누구에게
        private String letter_read;      // 편지 읽음 여부
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchLetterTo {
        private String letter_to;        // 누구에게
        private String member_to;        // 받는 사람 구분자
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchLetterList {
        private String letter_to;        // 누구에게
        private String member_to;        // 받는 사람 구분자
        private String letter_from;        // 누구로부터
        private String member_from;        // 보낸 사람 구분자
    }

    @Getter
    @Builder
    public static class SearchLetter {
        private Long letter_seq;         // 구분자
        private String letter_to;        // 누구에게
        private String letter_from;      // 누구로부터
        private String letter_contents;  // 편지 내용
        private String letter_read;      // 편지 읽음 여부
        private String member_to;        // 받는 사람 구분자
        private String member_from;      // 보낸 사람 구분자
        private String paper_seq;        // 편지지 구분자
    }
}
