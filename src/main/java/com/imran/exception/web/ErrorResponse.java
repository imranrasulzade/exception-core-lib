package com.imran.exception.web;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

public class ErrorResponse {

    private final String code;
    private final String message;
    private final String traceId;
    private final String path;
    private final int status;
    private final Instant timestamp;
    private final Map<String, Object> details;

    public ErrorResponse(String code,
                         String message,
                         String traceId,
                         String path,
                         int status,
                         Instant timestamp,
                         Map<String, Object> details) {
        this.code = code;
        this.message = message;
        this.traceId = traceId;
        this.path = path;
        this.status = status;
        this.timestamp = timestamp;
        this.details = details != null ? Collections.unmodifiableMap(details) : Collections.emptyMap();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getPath() {
        return path;
    }

    public int getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public static ErrorResponse of(String code,
                                   String message,
                                   String traceId,
                                   String path,
                                   int status,
                                   Map<String, Object> details) {
        return new ErrorResponse(
                code,
                message,
                traceId,
                path,
                status,
                Instant.now(),
                details
        );
    }
}
