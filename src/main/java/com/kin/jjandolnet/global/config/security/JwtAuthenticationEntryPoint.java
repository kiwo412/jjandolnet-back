package com.kin.jjandolnet.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kin.jjandolnet.global.error.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 필터에서 저장한 에러 코드를 가져옴
        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");

        // 기본 401 에러 - 잘못된 접근입니다.
        if (errorCode == null) {
            errorCode = ErrorCode.UNAUTHORIZED;
        }

        // 응답 설정
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 전송

        Map<String, Object> body = new HashMap<>();

        if ("T002".equals(errorCode.getCode())) {
            body.put("code", errorCode.getCode());
            body.put("message", errorCode.getMessage());
        } else {
            body.put("code", errorCode.getCode());
            body.put("message", errorCode.getMessage());
        }

        // JSON으로 변환하여 응답
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
