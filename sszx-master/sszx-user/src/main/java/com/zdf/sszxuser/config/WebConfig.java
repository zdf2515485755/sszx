package com.zdf.sszxuser.config;

import com.zdf.sszxuser.interceptor.LogInInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *@Description WebConfig
 *@Author mrzhang
 *@Date 2024/5/22 21:43
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public LogInInterceptor logInInterceptor(){
        return new LogInInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInInterceptor())
                .addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
