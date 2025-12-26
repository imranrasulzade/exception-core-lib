package com.imran.exception;

import org.springframework.http.HttpStatus;


/**
 * @author Imran Rasulzade
 * @Date 2025/12/25
 */


public enum CommonErrorCode implements ErrorCode {

    INTERNAL_ERROR("INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "errors.internal"),
    VALIDATION_ERROR("VALIDATION_ERROR", HttpStatus.BAD_REQUEST, "errors.validation"),
    NOT_FOUND("NOT_FOUND", HttpStatus.NOT_FOUND, "errors.not_found"),
    BAD_REQUEST("BAD_REQUEST", HttpStatus.BAD_REQUEST, "errors.bad_request"),
    CONFLICT("CONFLICT", HttpStatus.CONFLICT, "errors.conflict");

    private final String code;
    private final HttpStatus httpStatus;
    private final String messageKey;

    CommonErrorCode(String code, HttpStatus httpStatus, String messageKey) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }
}
