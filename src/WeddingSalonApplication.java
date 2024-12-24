/**
 * Основной класс приложения.
 * Инициализирует Spring Boot приложение и настраивает локаль, кодировщик паролей.
 */
package com.example.weddingsalon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;

@SpringBootApplication
public class WeddingSalonApplication {

    /**
     * Точка входа в приложение.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(WeddingSalonApplication.class, args);
    }

    /**
     * Бин кодировщика паролей.
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Локальный разрешитель для поддержания выбранной локали.
     * @return CookieLocaleResolver с настройками локали
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag("ru"));
        resolver.setCookieName("lang");
        resolver.setCookieMaxAge(3600 * 24 * 30);
        return resolver;
    }

    /**
     * Интерсептор для смены локали по параметру "lang".
     * @return LocaleChangeInterceptor
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * Добавляет интерсептор для смены локали в реестр.
     * @param registry реестр интерсепторов
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
