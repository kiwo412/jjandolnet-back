package com.kin.jjandolnet.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        String exception = (String) request.getAttribute("expired");

        // 응답 설정
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 전송

        Map<String, Object> body = new HashMap<>();

        if ("T002".equals(exception)) { // 만료된 경우 (ErrorCode.EXPIRED_TOKEN.getCode() 값)
            body.put("code", "T002");
            body.put("message", "Access Token Expired");
        } else {
            body.put("code", "T001");
            body.put("message", "Invalid Token or Unauthorized");
        }

        // JSON으로 변환하여 응답
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
