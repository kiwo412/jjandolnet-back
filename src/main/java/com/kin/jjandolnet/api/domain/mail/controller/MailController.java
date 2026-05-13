package com.kin.jjandolnet.api.domain.mail.controller;

import com.kin.jjandolnet.api.domain.mail.service.MailService;
import com.kin.jjandolnet.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "03. MailController", description = "메일 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @Operation(summary = "비밀번호 찾기 - 임시 비밀번호 이메일 전송",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{\"email\": \"user@example.com\"}"
                            )
                    )
            )
    )
    @PostMapping("/sendTempPw")
    public ResponseEntity<ApiResponse<Void>> sendTempPw(@RequestBody Map<String, String> request) {
        mailService.sendTempPw(request);
        return ResponseEntity.ok(ApiResponse.success("임시 비밀번호가 이메일로 발송 되었습니다."));
    }
}
