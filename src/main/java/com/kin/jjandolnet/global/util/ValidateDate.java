package com.kin.jjandolnet.global.util;

import com.kin.jjandolnet.global.constant.SystemConstant;
import com.kin.jjandolnet.global.error.exception.BusinessException;
import com.kin.jjandolnet.global.error.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ValidateDate {

    //서비스 시작일 이전의 날짜 혹은 오늘 이후의 날짜 예외 처리
    public void validateNotFutureAndServiceStartDate(LocalDate date) {
        LocalDate today = LocalDate.now();
        LocalDate serviceStartDate = SystemConstant.SERVICE_START_DATE.getValue();

        if (date.isAfter(today) || date.isBefore(serviceStartDate)) {
            throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
        }
    }

}
