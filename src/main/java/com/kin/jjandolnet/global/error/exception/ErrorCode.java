package com.kin.jjandolnet.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통 에러
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
    INVALID_JSON_FORMAT(400, "C002", "JSON 파싱 중 에러가 발생했습니다."),
    METHOD_NOT_ALLOWED(405, "C003", "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR(500, "C004", "서버 내부 에러입니다."),

    // 유저 관련 에러
    EMAIL_DUPLICATION(400, "U001", "이미 존재하는 이메일입니다."),
    USER_NOT_FOUND(404, "U002", "사용자를 찾을 수 없습니다.");

    private final int status;
    private final String code;
    private final String message;
}
