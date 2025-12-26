package com.imran.exception.web;

import com.imran.exception.BaseException;
import com.imran.exception.CommonErrorCode;
import com.imran.exception.config.ExceptionMessageResolver;
import com.imran.exception.config.ExceptionProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionMessageResolver messageResolver;
    private final ExceptionProperties properties;

    public GlobalExceptionHandler(ExceptionMessageResolver messageResolver,
                                  ExceptionProperties properties) {
        this.messageResolver = messageResolver;
        this.properties = properties;
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex,
                                                             HttpServletRequest request) {
        Locale locale = LocaleContextHolder.getLocale();
        String traceId = MDC.get(properties.getTraceIdMdcKey());

        String message = messageResolver.resolve(ex.getErrorCode(), locale, ex.getMessageArgs());

        ErrorResponse body = ErrorResponse.of(
                ex.getErrorCode().getCode(),
                message,
                traceId,
                request.getRequestURI(),
                ex.getErrorCode().getHttpStatus().value(),
                ex.getDetails()
        );

        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest request) {
        Locale locale = LocaleContextHolder.getLocale();
        String traceId = MDC.get(properties.getTraceIdMdcKey());

        Map<String, Object> details = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        details.put("fieldErrors", fieldErrors);

        String message = messageResolver.resolve(
                CommonErrorCode.VALIDATION_ERROR,
                locale,
                new Object[0]
        );

        ErrorResponse body = ErrorResponse.of(
                CommonErrorCode.VALIDATION_ERROR.getCode(),
                message,
                traceId,
                request.getRequestURI(),
                CommonErrorCode.VALIDATION_ERROR.getHttpStatus().value(),
                details
        );

        return ResponseEntity.status(CommonErrorCode.VALIDATION_ERROR.getHttpStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex,
                                                          HttpServletRequest request) {
        Locale locale = LocaleContextHolder.getLocale();
        String traceId = MDC.get(properties.getTraceIdMdcKey());

        String message = messageResolver.resolve(
                CommonErrorCode.INTERNAL_ERROR,
                locale,
                new Object[0]
        );

        ErrorResponse body = ErrorResponse.of(
                CommonErrorCode.INTERNAL_ERROR.getCode(),
                message,
                traceId,
                request.getRequestURI(),
                CommonErrorCode.INTERNAL_ERROR.getHttpStatus().value(),
                Map.of("error", ex.getClass().getSimpleName())
        );

        return ResponseEntity.status(CommonErrorCode.INTERNAL_ERROR.getHttpStatus()).body(body);
    }
}
