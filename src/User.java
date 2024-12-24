/**
 * Сущность пользователя системы.
 * Содержит имя пользователя, email, пароль и роль.
 */
package com.example.weddingsalon;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя (уникальное).
     */
    @Column(nullable = false)
    private String username;

    /**
     * Email пользователя (уникальный).
     */
    @Column(nullable = false)
    private String email;

    /**
     * Зашифрованный пароль пользователя.
     */
    private String password;

    /**
     * Роль пользователя.
     */
    private String role;

    /**
     * Защищённый конструктор без аргументов.
     */
    protected User() {
    }
}
