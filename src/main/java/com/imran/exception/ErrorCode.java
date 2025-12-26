package com.imran.exception;

import org.springframework.http.HttpStatus;


/**
 * @author Imran Rasulzade
 * @Date 2025/12/25
 */


public interface ErrorCode {

    /**
     * Business code, e.g. USER_NOT_FOUND, VALIDATION_ERROR.
     */
    String getCode();

    /**
     * HttpStatus to return.
     */
    HttpStatus getHttpStatus();

    /**
     * Message key inside messages properties, e.g. errors.user.not_found
     */
    String getMessageKey();
}
