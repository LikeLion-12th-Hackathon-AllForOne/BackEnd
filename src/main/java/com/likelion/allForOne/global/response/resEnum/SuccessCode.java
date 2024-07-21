package com.likelion.allForOne.global.response.resEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    // COMMON
    FOUND_LIST(201, "조회가 완료되었습니다."),
    FOUND_NO_SEARCH_RESULT(200, "조회결과가 없습니다."),

    ;

    private final Integer code;
    private final String message;
}
