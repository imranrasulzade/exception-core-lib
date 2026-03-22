package com.imran.exception.web;

import com.imran.exception.BusinessException;
import com.imran.exception.CommonErrorCode;
import com.imran.exception.config.ExceptionMessageResolver;
import com.imran.exception.config.ExceptionProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private static final String TRACE_ID = "trace-123";

    @AfterEach
    void clearMdc() {
        MDC.clear();
        LocaleContextHolder.resetLocaleContext();
    }

    @Test
    void mapsBaseExceptionToExpectedBody() {
        ExceptionProperties properties = new ExceptionProperties();
        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage(CommonErrorCode.BAD_REQUEST.getMessageKey(), Locale.ENGLISH, "Bad request");

        GlobalExceptionHandler handler = new GlobalExceptionHandler(
                new ExceptionMessageResolver(messageSource),
                properties
        );

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/42");
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        MDC.put(properties.getTraceIdMdcKey(), TRACE_ID);

        var response = handler.handleBaseException(new BusinessException(CommonErrorCode.BAD_REQUEST), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(CommonErrorCode.BAD_REQUEST.getCode());
        assertThat(response.getBody().getMessage()).isEqualTo("Bad request");
        assertThat(response.getBody().getTraceId()).isEqualTo(TRACE_ID);
        assertThat(response.getBody().getPath()).isEqualTo("/users/42");
    }
}
