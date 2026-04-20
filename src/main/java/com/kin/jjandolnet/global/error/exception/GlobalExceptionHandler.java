package com.kin.jjandolnet.global.error.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * @Valid 또는 @Validated 검증 예외 처리 (400 Bad Request)
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        log.error("handleMethodArgumentNotValidException", ex);

        final ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

        String errorMessage;
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        if (!fieldErrors.isEmpty() && fieldErrors.get(0).getDefaultMessage() != null && !fieldErrors.get(0).getDefaultMessage().isBlank()) {
            errorMessage = fieldErrors.get(0).getDefaultMessage();
        } else {
            errorMessage = errorCode.getMessage();
        }
        log.error("errorMessage: {}", errorMessage);
        
        final ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorMessage)
                .error(errorCode.name())
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생 (405 Method Not Allowed)
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatusCode status,
                                                                         WebRequest request) {
        log.error("handleHttpRequestMethodNotSupportedException", ex);
        final ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        final ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .error(errorCode.name())
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    /**
     * JSON 파싱 에러 또는 데이터 타입이 맞지 않을 때 발생 (400 Bad Request)
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        log.error("handleHttpMessageNotReadableException", ex);
        final ErrorCode errorCode = ErrorCode.INVALID_JSON_FORMAT;
        final ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .error(errorCode.name())
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    /**
     * 비즈니스 로직 에러 처리
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("BusinessException: {}", e.getErrorCode().getMessage());
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .error(errorCode.name())
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    /**
     * 그 외
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unhandled Exception: ", e);
        final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        final ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .error(errorCode.name())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
