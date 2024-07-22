package com.likelion.allForOne.global.response.resEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 4xx
    INVALID_PARAMETER(400, HttpStatus.BAD_REQUEST, "파라미터 값을 다시 확인해주세요."),
    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "액세스 권한이 없습니다."),
    RESOURCE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Resource not found"),
    // 5xx
    CREATE_FAIL(500, HttpStatus.INTERNAL_SERVER_ERROR, "객체 생성에 실패했습니다.")
    ;

    private final Integer code;
    private final HttpStatus status;
    private final String message;
}
