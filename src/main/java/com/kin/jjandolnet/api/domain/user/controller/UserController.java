package com.kin.jjandolnet.api.domain.user.controller;

import com.kin.jjandolnet.api.domain.auth.UserPrincipal;
import com.kin.jjandolnet.api.domain.user.dto.AddressDto;
import com.kin.jjandolnet.api.domain.user.dto.JobDto;
import com.kin.jjandolnet.api.domain.user.dto.UserDto;
import com.kin.jjandolnet.api.domain.user.service.UserService;
import com.kin.jjandolnet.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

    @GetMapping("/myPage")
    public ResponseEntity<ApiResponse<UserDto.Response>> getUserDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        UserDto.Response response = userService.getUserDetail(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("내 정보 조회가 완료되었습니다.", response));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody UserDto.CreateRequest request) {
        userService.register(request);

        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
    }

    @PostMapping("/findEmail")
    public ResponseEntity<ApiResponse<String>> findEmail(@Valid @RequestBody UserDto.FindIdRequest request) {
        String email = userService.findEmail(request);
        return ResponseEntity.ok(ApiResponse.success("아이디 찾기가 완료되었습니다.",email));
    }

    @PutMapping("/myPage")
    public ResponseEntity<ApiResponse<Void>> updateUserDetail(
            @Valid @RequestBody UserDto.UpdateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.updateUserDetail(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("내 정보가 수정되었습니다."));
    }

}
