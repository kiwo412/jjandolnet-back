package com.kin.jjandolnet.api.domain.user.controller;

import com.kin.jjandolnet.api.domain.user.dto.UserDto;
import com.kin.jjandolnet.api.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserDto.CreateRequest request) {
        userService.register(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}
