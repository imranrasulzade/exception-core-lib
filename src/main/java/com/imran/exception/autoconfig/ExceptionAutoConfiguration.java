package com.imran.exception.autoconfig;

import com.imran.exception.config.ExceptionMessageResolver;
import com.imran.exception.config.ExceptionProperties;
import com.imran.exception.log.ExceptionLoggingAspect;
import com.imran.exception.web.GlobalExceptionHandler;
import com.imran.exception.web.TraceIdFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;


/**
 * @author Imran Rasulzade
 * @Date 2025/12/24
 */


@AutoConfiguration
@EnableConfigurationProperties(ExceptionProperties.class)
public class ExceptionAutoConfiguration {

    /**
     * Dedicated MessageSource for error messages.
     * User can override by defining a bean with the same name.
     */
    @Bean(name = "imranExceptionMessageSource")
    @ConditionalOnMissingBean(name = "imranExceptionMessageSource")
    public MessageSource imranExceptionMessageSource(ExceptionProperties properties) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(properties.getMessagesBasename());
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionMessageResolver exceptionMessageResolver(MessageSource imranExceptionMessageSource) {
        return new ExceptionMessageResolver(imranExceptionMessageSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public TraceIdFilter traceIdFilter(ExceptionProperties properties) {
        return new TraceIdFilter(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler(ExceptionMessageResolver resolver,
                                                         ExceptionProperties properties) {
        return new GlobalExceptionHandler(resolver, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionLoggingAspect exceptionLoggingAspect(ExceptionProperties properties) {
        return new ExceptionLoggingAspect(properties);
    }
}
