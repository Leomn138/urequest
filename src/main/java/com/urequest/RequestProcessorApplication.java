package com.urequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableConfigurationProperties
@Configuration
@EnableCaching
public class RequestProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(RequestProcessorApplication.class, args);
    }
}
