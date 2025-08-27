package com.example.library.config;

import com.example.library.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // <-- Quan trọng: bật @PreAuthorize / @PostAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;                    // Filter kiểm tra & gán JWT
    private final AuthenticationConfiguration authConfig; // Lấy AuthenticationManager

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Khuyến nghị cho REST stateless
                .csrf(csrf -> csrf.disable())

                // Cho phép/chặn URL
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/auth/**"),        // Đăng nhập / đăng ký
                                new AntPathRequestMatcher("/swagger-ui/**"),  // Swagger UI
                                new AntPathRequestMatcher("/v3/api-docs/**")  // OpenAPI JSON
                        ).permitAll()
                        .anyRequest().authenticated() // Các request khác cần JWT + sẽ bị @PreAuthorize kiểm tra quyền
                )

                // Hệ thống stateless (không session)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Thêm JwtFilter trước filter xác thực mặc định
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // Giữ các cấu hình mặc định khác (headers, cors…)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /** Cung cấp AuthenticationManager cho AuthController */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
