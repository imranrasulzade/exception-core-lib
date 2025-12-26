package com.imran.exception;


/**
 * @author Imran Rasulzade
 * @Date 2025/12/25
 */


public class BusinessException extends BaseException {

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
