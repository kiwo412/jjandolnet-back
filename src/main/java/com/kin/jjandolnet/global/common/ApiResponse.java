package com.kin.jjandolnet.global.common;

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
}
