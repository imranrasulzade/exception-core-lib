package com.imran.exception.autoconfig;

import com.imran.exception.config.ExceptionMessageResolver;
import com.imran.exception.config.ExceptionProperties;
import com.imran.exception.log.ExceptionLoggingAspect;
import com.imran.exception.web.GlobalExceptionHandler;
import com.imran.exception.web.TraceIdFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ExceptionAutoConfiguration.class));

    @Test
    void registersCoreBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(ExceptionProperties.class);
            assertThat(context).hasBean("imranExceptionMessageSource");
            assertThat(context).hasSingleBean(ExceptionMessageResolver.class);
            assertThat(context).hasSingleBean(TraceIdFilter.class);
            assertThat(context).hasSingleBean(GlobalExceptionHandler.class);
            assertThat(context).hasSingleBean(ExceptionLoggingAspect.class);
        });
    }

    @Test
    void keepsUserProvidedMessageSource() {
        contextRunner
                .withBean("imranExceptionMessageSource", MessageSource.class, () -> new MessageSource() {
                    @Override
                    public String getMessage(String code, Object[] args, String defaultMessage, java.util.Locale locale) {
                        return "custom";
                    }

                    @Override
                    public String getMessage(String code, Object[] args, java.util.Locale locale) throws NoSuchMessageException {
                        return "custom";
                    }

                    @Override
                    public String getMessage(org.springframework.context.MessageSourceResolvable resolvable, java.util.Locale locale)
                            throws NoSuchMessageException {
                        return "custom";
                    }
                })
                .run(context -> {
                    MessageSource messageSource = context.getBean("imranExceptionMessageSource", MessageSource.class);
                    assertThat(messageSource.getMessage("code", null, "fallback", null)).isEqualTo("custom");
                });
    }
}
