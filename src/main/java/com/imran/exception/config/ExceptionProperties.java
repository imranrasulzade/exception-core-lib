package com.imran.exception.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author Imran Rasulzade
 * @Date 2025/12/25
 */


@ConfigurationProperties(prefix = "imran.exception")
public class ExceptionProperties {

    /**
     * Message bundle basename for error messages.
     * Example: "i18n/errors" -> files: i18n/errors.properties, i18n/errors_az.properties, ...
     */
    private String messagesBasename = "i18n/errors";

    /**
     * HTTP header name used for Trace-Id.
     */
    private String traceIdHeaderName = "X-Trace-Id";

    /**
     * MDC key for storing traceId.
     */
    private String traceIdMdcKey = "traceId";

    /**
     * Whether to log full stacktrace for unexpected exceptions.
     */
    private boolean logStackTrace = true;

    public String getMessagesBasename() {
        return messagesBasename;
    }

    public void setMessagesBasename(String messagesBasename) {
        this.messagesBasename = messagesBasename;
    }

    public String getTraceIdHeaderName() {
        return traceIdHeaderName;
    }

    public void setTraceIdHeaderName(String traceIdHeaderName) {
        this.traceIdHeaderName = traceIdHeaderName;
    }

    public String getTraceIdMdcKey() {
        return traceIdMdcKey;
    }

    public void setTraceIdMdcKey(String traceIdMdcKey) {
        this.traceIdMdcKey = traceIdMdcKey;
    }

    public boolean isLogStackTrace() {
        return logStackTrace;
    }

    public void setLogStackTrace(boolean logStackTrace) {
        this.logStackTrace = logStackTrace;
    }
}
