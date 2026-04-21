package com.kin.jjandolnet.api.domain.auth.service;

import com.kin.jjandolnet.api.domain.auth.dto.AuthDto;
import com.kin.jjandolnet.api.domain.auth.dto.TokenDto;
import com.kin.jjandolnet.api.domain.auth.exception.AuthException;
import com.kin.jjandolnet.api.domain.auth.jwt.TokenProvider;
import com.kin.jjandolnet.global.error.exception.BusinessException;
import com.kin.jjandolnet.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Transactional
    public TokenDto login(AuthDto.LoginRequest request) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        try {
            // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
            // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            return tokenProvider.generateTokenDto(authentication);
        } catch (BadCredentialsException e) {
            throw new AuthException.LoginFailedException();
        }
    }

    @Transactional
    public TokenDto refresh(String refreshToken) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // 2. Refresh Token 에서 이메일 추출
        String email = tokenProvider.getEmailFromToken(refreshToken);

        // 3. 해당 사용자의 권한 정보 가져오기 (UserDetails 기반)
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        // 4. 새로운 토큰 쌍 발급
        return tokenProvider.generateTokenDto(authentication);
    }
}
