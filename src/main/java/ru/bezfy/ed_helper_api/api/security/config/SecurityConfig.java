package ru.bezfy.ed_helper_api.api.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.bezfy.ed_helper_api.api.security.filter.JWTRequestFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JWTRequestFilter jwtRequestFilter;

    public SecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable(); // Отключаем CSRF
        http.cors().configurationSource(corsConfigurationSource()); // Включаем CORS
        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);
        http.authorizeHttpRequests()
                .requestMatchers("/v1/auth/**", "/v1/media/**").permitAll() // Разрешаем доступ без аутентификации
                .anyRequest().authenticated(); // Все остальные запросы требуют аутентификации
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Разрешить все домены (или укажите конкретные, например, "http://localhost:8080")
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Разрешенные HTTP-методы
        configuration.setAllowedHeaders(Arrays.asList("*")); // Разрешить все заголовки
        configuration.setAllowCredentials(false); // Не разрешать учетные данные (если не требуется)
        configuration.setMaxAge(3600L); // Время кэширования CORS-запросов

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Применить настройки CORS ко всем путям
        return source;
    }
}