package com.imran.exception;

import java.util.Collections;
import java.util.Map;


/**
 * @author Imran Rasulzade
 * @Date 2025/12/25
 */


public abstract class BaseException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object[] messageArgs;
    private final Map<String, Object> details;

    protected BaseException(ErrorCode errorCode) {
        this(errorCode, null, null);
    }

    protected BaseException(ErrorCode errorCode, Object[] messageArgs) {
        this(errorCode, messageArgs, null);
    }

    protected BaseException(ErrorCode errorCode, Object[] messageArgs, Map<String, Object> details) {
        super(errorCode != null ? errorCode.getCode() : null);
        this.errorCode = errorCode;
        this.messageArgs = messageArgs != null ? messageArgs : new Object[0];
        this.details = details != null ? Collections.unmodifiableMap(details) : Collections.emptyMap();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}
