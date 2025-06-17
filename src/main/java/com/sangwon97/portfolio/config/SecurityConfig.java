package com.sangwon97.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/","/portfolio/**", "/index", "/home", "/favicon.ico",
                "/css/**", "/js/**", "/images/**",
                "/api/login", "/api/logout",
                "/board/list", "/board/view/**"
            ).permitAll()
            .requestMatchers(HttpMethod.GET, "/board/write**").authenticated()
            .requestMatchers(HttpMethod.POST, "/board/write").authenticated()
            .requestMatchers("/board/delete/**").authenticated()
            .anyRequest().authenticated()
        )

            .logout(logout -> logout
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/") // ✅ 로그아웃 후 이동할 주소
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                })
            );

        return http.build();
    }
}
