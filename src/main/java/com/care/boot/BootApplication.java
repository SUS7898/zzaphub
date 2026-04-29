package com.care.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder; // 추가됨
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer; // 추가됨

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class BootApplication extends SpringBootServletInitializer { // 1. 상속 추가

    // 2. 외장 톰캣이 이 앱을 시작할 수 있게 해주는 설정 추가
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BootApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }
}