package com.imran.exception;


/**
 * @author Imran Rasulzade
 * @Date 2025/12/25
 */


public class NotFoundException extends BaseException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
