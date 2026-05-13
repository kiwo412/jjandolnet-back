package com.kin.jjandolnet.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통 예외
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
    INVALID_JSON_FORMAT(400, "C002", "JSON 파싱 중 에러가 발생했습니다."),
    METHOD_NOT_ALLOWED(405, "C003", "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR(500, "C004", "서버 내부 에러입니다."),
    HANDLE_ACCESS_DENIED(403, "C005", "권한이 없습니다."),
    UNAUTHORIZED(401, "C006", "로그인이 필요합니다."),

    // 토큰 관련 예외
    INVALID_TOKEN(401, "T001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(401, "T002", "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(401, "T003", "지원되지 않는 토큰입니다."),
    TOKEN_NOT_FOUND(401, "T004", "토큰이 존재하지 않습니다."),
    INVALID_AUTHORITIES_KEY_TOKEN(401, "T005", "권한 정보가 없는 토큰입니다."),

    // 유저 관련 예외
    EMAIL_DUPLICATION(400, "U001", "이미 존재하는 이메일입니다."),
    NICKNAME_DUPLICATION(400, "U002", "이미 존재하는 닉네임입니다."),
    USER_NOT_FOUND(404, "U003", "사용자를 찾을 수 없습니다."),
    ROLE_NOT_FOUND(404, "U004", "기본 권한을 찾을 수 없습니다."),
    ADDRESS_NOT_FOUND(404, "U005", "거주 지역 정보를 찾을 수 없습니다."),
    JOB_NOT_FOUND(404, "U006", "직업 정보를 찾을 수 없습니다."),
    UNDERAGE(400, "U007", "만 14세 미만은 서비스에 가입할 수 없습니다."),

    // 소비, 수입 관련 예외
    CATEGORY_NOT_FOUND(404, "E001", "카테고리를 찾을 수 없습니다."),
    EXPENSE_NOT_FOUND(404, "E002", "해당 소비 내역을 찾을 수 없습니다."),
    INCOME_NOT_FOUND(404, "E003", "해당 수입 내역을 찾을 수 없습니다."),
    INCOME_EXPENSE_ZERO(400, "E004", "수입과 소비내역이 있어야 확인 할 수 있습니다."),

    // 게시판 관련 예외
    POST_NOT_FOUND(404, "B001", "해당 게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(404, "B002", "해당 댓글을 찾을 수 없습니다."),

    //날짜 관련 예외
    INVALID_DATE_RANGE(400, "D001", "잘못된 날짜입니다.");

    private final int status;
    private final String code;
    private final String message;
}
