package com.kin.jjandolnet.api.domain.user.exception;

import com.kin.jjandolnet.global.error.exception.BusinessException;
import com.kin.jjandolnet.global.error.exception.ErrorCode;

public class UserException extends BusinessException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class DuplicateEmailException extends UserException {
        public DuplicateEmailException() {
            super(ErrorCode.EMAIL_DUPLICATION);
        }
    }

    public static class DuplicateNicknameException extends UserException {
        public DuplicateNicknameException() {
            super(ErrorCode.NICKNAME_DUPLICATION);
        }
    }


}
