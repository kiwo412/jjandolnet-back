package com.kin.jjandolnet.global.constant;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public enum SystemConstant {

    /**
     * 서비스 시작일 (2026년 1월 1일)
     * 2026-01-01 이전은 추후 지원 예정 정책
     */
    SERVICE_START_DATE(LocalDate.of(2026, 1, 1));

    private final LocalDate value;

    SystemConstant(LocalDate value) {
        this.value = value;
    }

}
