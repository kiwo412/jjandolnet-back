package com.kin.jjandolnet.api.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

public class TokenDto {

    @Getter
    @Builder
    public static class Response {
        private String accessToken;
        private String refreshToken;
        private String grantType;
        private String uuid;
        private String nickname;
    }
}