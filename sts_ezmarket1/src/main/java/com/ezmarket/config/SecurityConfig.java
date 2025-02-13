package com.ezmarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import google.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final UserService userService;

    public SecurityConfig(CorsConfig corsConfig, UserService userService) {
        this.corsConfig = corsConfig;
        this.userService = userService;
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
            .sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("http://localhost:3000/", true)
                .successHandler((request, response, authentication) -> {
                	OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
                    String provider = authToken.getAuthorizedClientRegistrationId();        
                    
                    String token = userService.saveUser(authToken.getPrincipal(), provider); 
                    
                    Cookie cookie = new Cookie("jwt_token", token);
    	            cookie.setPath("/"); 
    	            cookie.setMaxAge(60 * 60); 
    	            response.addCookie(cookie);
                    
                    response.setHeader("Authorization", "Bearer " + token);  
                    response.sendRedirect("http://localhost:3000/");
                })
            );
        
        return http.build();
    }
}
