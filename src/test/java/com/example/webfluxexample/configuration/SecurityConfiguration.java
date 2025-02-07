package com.example.webfluxexample.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Конфигурация безопасности для приложения.
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    /**
     * Создает и возвращает экземпляр PasswordEncoder для шифрования паролей.
     *
     * @return экземпляр PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Создает и возвращает цепочку фильтров безопасности для in-memory аутентификации.
     *
     * @param http экземпляр ServerHttpSecurity
     * @return экземпляр SecurityWebFilterChain
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.security", name = "type", havingValue = "inMemory")
    public SecurityWebFilterChain inMemoryFilterChain(ServerHttpSecurity http) {
        return buildDefaultHttpSecurity(http).build();
    }

    /**
     * Создает и возвращает сервис пользователей для in-memory аутентификации.
     *
     * @param passwordEncoder экземпляр PasswordEncoder
     * @return экземпляр ReactiveUserDetailsService
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.security", name = "type", havingValue = "inMemory")
    public ReactiveUserDetailsService inMemoryUserDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("12345"))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("11111"))
                .roles("ADMIN")
                .build();
        return new MapReactiveUserDetailsService(user, admin);
    }

    /**
     * Создает и возвращает менеджер аутентификации для базы данных.
     *
     * @param userDetailsService сервис пользователей для аутентификации
     * @return экземпляр ReactiveAuthenticationManager
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.security", name = "type", havingValue = "db")
    public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService) {
        var reactiveAuthenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        reactiveAuthenticationManager.setPasswordEncoder(passwordEncoder());
        return reactiveAuthenticationManager;
    }

    /**
     * Создает и возвращает цепочку фильтров безопасности для аутентификации через базу данных.
     *
     * @param http                  экземпляр ServerHttpSecurity
     * @param authenticationManager менеджер аутентификации
     * @return экземпляр SecurityWebFilterChain
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.security", name = "type", havingValue = "db")
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveAuthenticationManager authenticationManager) {
        return buildDefaultHttpSecurity(http)
                .authenticationManager(authenticationManager)
                .build();
    }

    /**
     * Строит и возвращает конфигурацию безопасности по умолчанию.
     *
     * @param http экземпляр ServerHttpSecurity
     * @return экземпляр ServerHttpSecurity
     */
    private ServerHttpSecurity buildDefaultHttpSecurity(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange((auth) -> auth.pathMatchers("/api/v1/public/**").permitAll()
                        .anyExchange().authenticated())
                .httpBasic(Customizer.withDefaults());
    }
}
