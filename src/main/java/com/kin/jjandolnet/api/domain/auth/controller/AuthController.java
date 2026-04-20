package com.kin.jjandolnet.api.domain.auth.controller;

import com.kin.jjandolnet.api.domain.auth.dto.AuthDto;
import com.kin.jjandolnet.api.domain.auth.dto.TokenDto;
import com.kin.jjandolnet.api.domain.auth.service.AuthService;
import com.kin.jjandolnet.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponse<TokenDto>> login(@Valid @RequestBody AuthDto.LoginRequest request) {
        TokenDto tokenDto = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success("로그인 되었습니다.", tokenDto)
        );
    }
}
