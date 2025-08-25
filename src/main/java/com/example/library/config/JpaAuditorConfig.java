package com.example.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaAuditorConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        // TODO: sau này lấy username từ JWT
        return () -> Optional.of("system");
    }
}
