/**
 * Контроллер для управления пользователями.
 * Обеспечивает регистрацию, отображение пользователей, назначение ролей.
 */
package com.example.weddingsalon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    /**
     * Сервис для работы с пользователями.
     */
    private final UserService userService;

    /**
     * Репозиторий для работы с пользователями.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Сервис, предоставляющий детали пользователей.
     */
    @Autowired
    private UserService userDetailsService;

    /**
     * Сервис для работы с платьями.
     */
    @Autowired
    private DressService dressService;

    /**
     * Конструктор с параметром userService.
     * @param userService сервис для работы с пользователями
     */
    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Отображает главную страницу.
     * @param model модель для передачи данных в шаблон
     * @return имя шаблона home
     */
    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("user", new User());
        return "home";
    }

    /**
     * Регистрирует нового пользователя.
     * @param user объект пользователя
     * @param confirmPassword подтверждение пароля
     * @param model модель для передачи данных в шаблон
     * @return перенаправление на главную страницу или возврат к home в случае ошибки
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               @RequestParam("confirmPassword") String confirmPassword,
                               Model model) {
        user.setRole("USER");

        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordMismatchError", "Пароли не сходятся");
            model.addAttribute("user", user);
            return "home";
        }

        if (!isValidEmail(user.getEmail())) {
            model.addAttribute("invalidEmailError", "Некорректная почта");
            model.addAttribute("user", user);
            return "home";
        }

        try {
            User savedUser = userService.save(user);
            UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
            org.springframework.security.authentication.UsernamePasswordAuthenticationToken authToken =
                    new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            return "redirect:/";
        } catch (UserService.UserAlreadyExistsException e) {
            model.addAttribute("userExistsError", e.getMessage());
            model.addAttribute("user", user);
            return "home";
        } catch (Exception e) {
            model.addAttribute("registrationError", "Ошибка при регистрации. Пожалуйста, попробуйте снова.");
            model.addAttribute("user", user);
            return "home";
        }
    }

    /**
     * Проверяет корректность email.
     * @param email email для проверки
     * @return true если email корректный, иначе false
     */
    private boolean isValidEmail(String email) {
        if (email == null || !email.contains("@")) {
            return false;
        }
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }
        String domain = parts[1];
        return domain.contains(".");
    }

    /**
     * Возвращает список всех пользователей (JSON).
     * Только для роли ADMIN.
     * @return список пользователей
     */
    @GetMapping("/user-management")
    @ResponseBody
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Назначает роль пользователю.
     * Только для роли ADMIN.
     * @param userId идентификатор пользователя
     * @param role роль для назначения
     * @return перенаправление на главную страницу
     */
    @PostMapping("/assign-role")
    public String assignRole(@RequestParam("userId") Long userId, @RequestParam("role") String role) {
        User userToUpdate = userService.findById(userId);
        if (userToUpdate != null) {
            userToUpdate.setRole(role);
            userService.updateUser(userToUpdate);
        }
        return "redirect:/";
    }

    /**
     * Глобальный @ModelAttribute для передачи нового пользователя в каждую модель по умолчанию.
     */
    @ControllerAdvice
    static class MergedControllerAdvice {
        /**
         * Создает новый объект User для каждой модели.
         * @return новый пользователь
         */
        @ModelAttribute("user")
        public User user() {
            return new User();
        }
    }

    /**
     * Глобальный обработчик исключений.
     * Перенаправляет на страницу ошибки при любых исключениях.
     */
    @ControllerAdvice
    static class MergedExceptionHandler {
        /**
         * Обрабатывает все исключения.
         * @param ex исключение
         * @param model модель для передачи сообщения об ошибке
         * @return имя шаблона error
         */
        @ExceptionHandler(Exception.class)
        public String handleAllExceptions(Exception ex, Model model) {
            model.addAttribute("errorMessage", "Произошла ошибка: " + ex.getMessage());
            return "error";
        }
    }
}
