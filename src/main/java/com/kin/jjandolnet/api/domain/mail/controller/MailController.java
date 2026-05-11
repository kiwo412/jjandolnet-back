package com.kin.jjandolnet.api.domain.mail.controller;

import com.kin.jjandolnet.api.domain.mail.service.MailService;
import com.kin.jjandolnet.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/sendTempPw")
    public ResponseEntity<ApiResponse<Void>> sendTempPw(@RequestBody Map<String, String> request) {
        mailService.sendTempPw(request);
        return ResponseEntity.ok(ApiResponse.success("임시 비밀번호가 이메일로 발송 되었습니다."));
    }
}
