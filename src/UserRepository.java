/**
 * Репозиторий для сущности User.
 * Предоставляет методы поиска пользователя по имени и email.
 */
package com.example.weddingsalon;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Находит пользователя по имени.
     * @param username имя пользователя
     * @return объект User или null, если не найден
     */
    User findByUsername(String username);

    /**
     * Находит пользователя по email.
     * @param email email пользователя
     * @return объект User или null, если не найден
     */
    User findByEmail(String email);
}
