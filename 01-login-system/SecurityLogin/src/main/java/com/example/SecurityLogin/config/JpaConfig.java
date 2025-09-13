package com.example.SecurityLogin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    /*
        JPA Auditing 활성화 - 이게 있어야 날짜가 자동으로 생성됨.
     */
}
