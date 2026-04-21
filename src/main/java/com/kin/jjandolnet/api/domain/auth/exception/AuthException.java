package com.kin.jjandolnet.api.domain.auth.exception;

import com.kin.jjandolnet.global.error.exception.BusinessException;
import com.kin.jjandolnet.global.error.exception.ErrorCode;

public class AuthException extends BusinessException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class LoginFailedException extends AuthException {
        public LoginFailedException() {
            super(ErrorCode.LOGIN_FAILED);
        }
    }


}
