package com.ezmarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfig corsConfig;

    public SecurityConfig(CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("SecurityConfig: SecurityFilterChain 설정 시작됨."); // 확인용 로그 추가
        http
            .cors(cors -> cors.disable()) // 기본 CORS 정책 비활성화
            .addFilterBefore(corsConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class) // CORS 필터 적용
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // OPTIONS 요청 허용
                .anyRequest().permitAll()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    System.out.println("SecurityConfig: 인증되지 않은 요청 감지 (401 Unauthorized)"); // 확인용 로그 추가
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"User not authenticated\"}");
                })
            )
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("http://localhost:3000/home", true)
            );

        return http.build();
    }




}
