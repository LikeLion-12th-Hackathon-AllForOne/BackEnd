package com.likelion.allForOne.global.response;

import com.likelion.allForOne.global.response.resEnum.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode code;
}
