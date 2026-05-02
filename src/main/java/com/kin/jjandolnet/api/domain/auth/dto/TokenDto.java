package com.kin.jjandolnet.api.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private String grantType;

    private String uuid;
    private String nickname;
}
