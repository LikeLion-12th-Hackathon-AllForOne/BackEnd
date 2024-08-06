package com.likelion.allForOne.login.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenDto {
    @Builder
    @Getter
    public static class responseDto{
        private String nickname;
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    public static class accessLogin{
        private String nickname;
    }
}
