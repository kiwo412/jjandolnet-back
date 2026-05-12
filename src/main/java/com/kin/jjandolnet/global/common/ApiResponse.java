package com.kin.jjandolnet.global.common;

import com.kin.jjandolnet.global.error.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status("SUCCESS")
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse<Void> success(String message) {
        return ApiResponse.<Void>builder()
                .status("SUCCESS")
                .message(message)
                .data(null)
                .build();
    }

    //refresh token fail
    public static <T> ApiResponse<T> tokenFail(ErrorCode errorCode) {
        return ApiResponse.<T>builder()
                .status("REFRESH_FAIL")
                .message(errorCode.getMessage())
                .data(null)
                .build();
    }

}
