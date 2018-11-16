package com.xyt.config;

import java.util.Date;
import java.util.Locale;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new Formatter<Date>() {
            @Override
            public Date parse(String date, Locale locale) {
                return new Date(Long.parseLong(date));
            }

            @Override
            public String print(Date date, Locale locale) {
                return Long.valueOf(date.getTime()).toString();
            }
        });
    }
}