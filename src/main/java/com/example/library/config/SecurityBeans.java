package com.example.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean bảo mật dùng chung.
 * Sử dụng DelegatingPasswordEncoder để Spring nhận biết các prefix
 * {bcrypt}, {noop}, v.v.  Khi register, mật khẩu sẽ được lưu dạng:
 * {bcrypt}$2a$10$...
 */
@Configuration
public class SecurityBeans {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();       }
}
