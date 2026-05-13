package com.kin.jjandolnet.api.domain.user.controller;

import com.kin.jjandolnet.api.domain.auth.UserPrincipal;
import com.kin.jjandolnet.api.domain.user.dto.AddressDto;
import com.kin.jjandolnet.api.domain.user.dto.JobDto;
import com.kin.jjandolnet.api.domain.user.dto.UserDto;
import com.kin.jjandolnet.api.domain.user.service.UserService;
import com.kin.jjandolnet.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "02. UserController", description = "사용자 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "거주지역 목록")
    @GetMapping("/addressList")
    public ResponseEntity<ApiResponse<List<AddressDto.Response>>> addressList() {
        List<AddressDto.Response> addressList = userService.getAddressList();
        return ResponseEntity.ok(ApiResponse.success("거주지역 목록 조회가 완료되었습니다.", addressList));
    }

    @Operation(summary = "직업 목록")
    @GetMapping("/jobList")
    public ResponseEntity<ApiResponse<List<JobDto.Response>>> jobList() {
        List<JobDto.Response> jobList = userService.getJobList();
        return ResponseEntity.ok(ApiResponse.success("직업 목록 조회가 완료되었습니다.", jobList));
    }

    @Operation(summary = "내 정보", security = @SecurityRequirement(name = "accessToken"))
    @GetMapping("/myPage")
    public ResponseEntity<ApiResponse<UserDto.Response>> getUserDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        UserDto.Response response = userService.getUserDetail(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("내 정보 조회가 완료되었습니다.", response));
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody UserDto.CreateRequest request) {
        userService.register(request);

        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
    }

    @Operation(summary = "아이디(email) 찾기")
    @PostMapping("/findEmail")
    public ResponseEntity<ApiResponse<String>> findEmail(@Valid @RequestBody UserDto.FindIdRequest request) {
        String email = userService.findEmail(request);
        return ResponseEntity.ok(ApiResponse.success("아이디 찾기가 완료되었습니다.",email));
    }

    @Operation(summary = "내 정보 수정", security = @SecurityRequirement(name = "accessToken"))
    @PutMapping("/myPage")
    public ResponseEntity<ApiResponse<Void>> updateUserDetail(
            @Valid @RequestBody UserDto.UpdateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.updateUserDetail(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("내 정보가 수정되었습니다."));
    }

}
