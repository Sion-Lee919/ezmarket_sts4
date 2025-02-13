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

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable()) 
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().permitAll()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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
