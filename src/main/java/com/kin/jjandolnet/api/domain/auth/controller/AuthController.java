package com.kin.jjandolnet.api.domain.auth.controller;

import com.kin.jjandolnet.api.domain.auth.dto.AuthDto;
import com.kin.jjandolnet.api.domain.auth.dto.TokenDto;
import com.kin.jjandolnet.api.domain.auth.service.AuthService;
import com.kin.jjandolnet.global.common.ApiResponse;
import com.kin.jjandolnet.global.error.exception.BusinessException;
import com.kin.jjandolnet.global.error.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody AuthDto.LoginRequest request) {
        TokenDto tokenDto = authService.login(request);
        return createTokenResponseWithCookie(tokenDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(HttpServletRequest request) {
        // 쿠키에서 refreshToken 추출
        String refreshToken = Arrays.stream(request.getCookies() == null ? new Cookie[0] : request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.TOKEN_NOT_FOUND));

        TokenDto tokenDto = authService.refresh(refreshToken);
        return createTokenResponseWithCookie(tokenDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)    // 0초로 설정하면 브라우저가 즉시 삭제함
                .build();

        ApiResponse<Void> response = ApiResponse.success("로그아웃 되었습니다.", null);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);


    }

    private ResponseEntity<ApiResponse<TokenResponse>> createTokenResponseWithCookie(TokenDto tokenDto) {
        // RefreshToken을 HttpOnly 쿠키에 저장
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(false) // 로컬 환경(HTTP)에서는 false, 운영(HTTPS) 환경에서는 true 권장
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7일 (TokenProvider 설정과 일치)
                .sameSite("Lax")
                .build();

        // AccessToken, uuid, nickname 담은 응답 객체 생성
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(tokenDto.getAccessToken())
                .grantType(tokenDto.getGrantType())
                .uuid(tokenDto.getUuid())
                .nickname(tokenDto.getNickname())
                .build();

        ApiResponse<TokenResponse> response = ApiResponse.success("로그인 성공했습니다.", tokenResponse);

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

        private String uuid;
        private String nickname;
    }
}
