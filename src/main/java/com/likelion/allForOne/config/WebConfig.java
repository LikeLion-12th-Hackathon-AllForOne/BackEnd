package com.likelion.allForOne.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("Cors Setting");
        registry.addMapping("/**")
                .allowedOrigins("http://13.124.253.134:8080", "http://localhost:8080")
                .allowedMethods("GET", "POST", "DELETE")
                .allowCredentials(true)
                .maxAge(3000);
    }
}
