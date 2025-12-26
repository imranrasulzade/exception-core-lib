package com.imran.exception.log;

import com.imran.exception.BaseException;
import com.imran.exception.config.ExceptionProperties;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


/**
 * @author Imran Rasulzade
 * @Date 2025/12/25
 */


@Aspect
public class ExceptionLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ExceptionLoggingAspect.class);

    private final ExceptionProperties properties;

    public ExceptionLoggingAspect(ExceptionProperties properties) {
        this.properties = properties;
    }

    /**
     * Pointcut: all public methods in @RestController, @Controller, @Service beans.
     */
    @AfterThrowing(pointcut =
            "within(@org.springframework.web.bind.annotation.RestController *) || " +
            "within(@org.springframework.stereotype.Controller *) || " +
            "within(@org.springframework.stereotype.Service *)",
            throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {

        String traceId = MDC.get(properties.getTraceIdMdcKey());
        String method = joinPoint.getSignature().toShortString();

        if (ex instanceof BaseException baseException) {
            String code = baseException.getErrorCode() != null
                    ? baseException.getErrorCode().getCode()
                    : "UNKNOWN";
            log.error("Handled BaseException: code={}, traceId={}, method={}",
                    code, traceId, method, baseException);
        } else {
            if (properties.isLogStackTrace()) {
                log.error("Unhandled exception: traceId={}, method={}, ex={}",
                        traceId, method, ex.getClass().getName(), ex);
            } else {
                log.error("Unhandled exception: traceId={}, method={}, ex={}",
                        traceId, method, ex.getClass().getName());
            }
        }
    }
}
