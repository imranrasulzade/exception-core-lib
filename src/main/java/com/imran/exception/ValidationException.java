package com.imran.exception;

import java.util.Map;


/**
 * @author Imran Rasulzade
 * @Date 2025/12/25
 */


public class ValidationException extends BaseException {

    public ValidationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ValidationException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public ValidationException(ErrorCode errorCode, Object[] args, Map<String, Object> details) {
        super(errorCode, args, details);
    }
}
