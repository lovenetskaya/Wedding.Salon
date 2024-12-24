/**
 * Сервис для работы с пользователями.
 * Реализует интерфейс UserDetailsService для интеграции со Spring Security.
 */
package com.example.weddingsalon;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    /**
     * Репозиторий для пользователей.
     */
    private final UserRepository userRepository;

    /**
     * Кодировщик паролей.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор сервиса с параметрами.
     * @param userRepository репозиторий пользователей
     * @param passwordEncoder кодировщик паролей
     */
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Загрузка пользователя по имени для Spring Security.
     * @param username имя пользователя
     * @return объект UserDetails
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                java.util.Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

    /**
     * Сохраняет нового пользователя.
     * @param user объект пользователя
     * @return сохранённый пользователь
     * @throws UserAlreadyExistsException если пользователь с таким именем или email уже существует
     */
    public User save(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Пользователь с таким именем уже существует. Выберите другое имя пользователя.");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException("Пользователь с такой почтой уже существует. Выберите другую почту.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Находит пользователя по идентификатору.
     * @param id идентификатор пользователя
     * @return объект User или null, если не найден
     */
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Обновляет данные пользователя.
     * @param user объект пользователя для обновления
     */
    public void updateUser(User user) {
        userRepository.save(user);
    }

    /**
     * Исключение для случая, когда пользователь уже существует.
     */
    public static class UserAlreadyExistsException extends RuntimeException {
        /**
         * Конструктор с сообщением об ошибке.
         * @param message сообщение об ошибке
         */
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
}
