/**
 * Конфигурация безопасности Spring Security.
 * Определяет правила доступа к ресурсам, настройки формы логина и защиты CSRF.
 */
package com.example.weddingsalon;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

@Configuration
public class SecurityConfig {

    /**
     * Кодировщик паролей.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Сервис, предоставляющий детали пользователей.
     */
    @Autowired
    private UserService userService;

    /**
     * Конфигурирует цепочку фильтров безопасности.
     * Определяет доступность ресурсов и поведение при аутентификации/логауте.
     * @param http объект конфигурации HttpSecurity
     * @return сконфигурированная цепочка фильтров
     * @throws Exception в случае ошибки конфигурации
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);

        AuthenticationManager authenticationManager = authBuilder.build();

        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        http
                .authenticationManager(authenticationManager)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/about", "/register", "/login", "/api/**").permitAll()
                        .requestMatchers("/dresses", "/histogram").hasAnyRole("USER", "MANAGER", "ADMIN")
                        .requestMatchers("/save", "/delete", "/update").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/user-management", "/assign-role").hasRole("ADMIN")
                        .requestMatchers("/designers/**").hasAnyRole("MANAGER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dresses", true)
                        .failureUrl("/?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                        .csrfTokenRequestHandler(requestHandler)
                );

        return http.build();
    }
}
