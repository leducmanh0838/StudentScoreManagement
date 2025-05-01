package com.ldm.configs;


import java.util.HashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.context.support.DelegatingMessageSource;

import java.util.Locale;
import java.util.Map;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
public class MessageConfig{
    
    @Bean
    public Map<String, MessageSource> customMessageSources() {
        Map<String, MessageSource> sources = new HashMap<>();

        sources.put("vi", createMessageSource("classpath:messages/messages_vi"));
        sources.put("en", createMessageSource("classpath:messages/messages_en"));

        return sources;
    }

    private MessageSource createMessageSource(String basename) {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename(basename);  // VD: classpath:messages/messages_1
        source.setDefaultEncoding("UTF-8");
        return source;
    }
}