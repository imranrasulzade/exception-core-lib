package com.imran.exception.config;

import com.imran.exception.ErrorCode;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.util.Locale;


/**
 * @author Imran Rasulzade
 * @Date 2025/12/24
 */


@Component
public class  ExceptionMessageResolver {

    private final MessageSource messageSource;

    public ExceptionMessageResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String resolve(ErrorCode errorCode, Locale locale, Object[] args) {
        if (errorCode == null) {
            return "UNKNOWN_ERROR";
        }
        try {
            return messageSource.getMessage(errorCode.getMessageKey(), args, locale);
        } catch (NoSuchMessageException ex) {
            // Fallback – show code if key not found
            return errorCode.getCode();
        }
    }
}
