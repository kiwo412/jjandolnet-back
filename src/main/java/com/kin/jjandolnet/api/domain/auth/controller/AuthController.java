package com.kin.jjandolnet.api.domain.auth.controller;

import com.kin.jjandolnet.api.domain.auth.dto.AuthDto;
import com.kin.jjandolnet.api.domain.auth.dto.TokenDto;
import com.kin.jjandolnet.api.domain.auth.service.AuthService;
import com.kin.jjandolnet.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody AuthDto.LoginRequest request) {
        TokenDto tokenDto = authService.login(request);

        // RefreshToken을 HttpOnly 쿠키에 저장 (보안 강화)
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(false) // 로컬 환경(HTTP)에서는 false, 운영(HTTPS) 환경에서는 true 권장
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7일 (TokenProvider 설정과 일치)
                .sameSite("Lax")
                .build();

        // AccessToken만 담은 응답 객체 생성
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .grantType(tokenDto.getGrantType())
                .build();

        ApiResponse<TokenResponse> response = ApiResponse.success("로그인에 성공하였습니다.", tokenResponse);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(response);
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TokenResponse {
        private String accessToken;
        private String grantType;
    }
}
