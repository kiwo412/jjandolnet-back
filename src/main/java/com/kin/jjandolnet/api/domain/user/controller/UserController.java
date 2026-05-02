package com.kin.jjandolnet.api.domain.user.controller;

import com.kin.jjandolnet.api.domain.user.dto.AddressDto;
import com.kin.jjandolnet.api.domain.user.dto.JobDto;
import com.kin.jjandolnet.api.domain.user.dto.UserDto;
import com.kin.jjandolnet.api.domain.user.service.UserService;
import com.kin.jjandolnet.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody UserDto.CreateRequest request) {
        userService.register(request);

        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
    }

    @GetMapping("/addressList")
    public ResponseEntity<ApiResponse<List<AddressDto.Response>>> addressList() {
        List<AddressDto.Response> addressList = userService.getAddressList();
        return ResponseEntity.ok(ApiResponse.success("거주지역 목록 조회가 완료되었습니다.", addressList));
    }

    @GetMapping("/jobList")
    public ResponseEntity<ApiResponse<List<JobDto.Response>>> jobList() {
        List<JobDto.Response> jobList = userService.getJobList();
        return ResponseEntity.ok(ApiResponse.success("직업 목록 조회가 완료되었습니다.", jobList));
    }


}
