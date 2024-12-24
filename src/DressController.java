/**
 * Контроллер для управления платьями через веб-интерфейс.
 * Обеспечивает отображение списка платьев, добавление, обновление, удаление и поиск.
 */
package com.example.weddingsalon;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DressController {

    /**
     * Сервис для работы с платьями.
     */
    @Autowired
    private DressService service;

    /**
     * Сервис для работы с дизайнерами.
     */
    @Autowired
    private DesignerService designerService;

    /**
     * Отображает главную страницу со списком платьев.
     * @param model модель для передачи данных в шаблон
     * @param request объект HttpServletRequest для получения сессии
     * @param keyword ключевое слово для поиска (опционально)
     * @return имя шаблона index
     */
    @GetMapping("/dresses")
    public String showDressesPage(Model model, HttpServletRequest request,
                                  @RequestParam(value = "keyword", required = false) String keyword) {
        request.getSession(true);

        model.addAttribute("dress", new Dress());
        model.addAttribute("user", new User());
        model.addAttribute("listDresses", service.listAll(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("listDesigners", designerService.findAll());

        Object loginError = request.getAttribute("loginError");
        Object username = request.getAttribute("username");

        if (loginError != null) {
            model.addAttribute("loginError", loginError);
        }
        if (username != null) {
            model.addAttribute("username", username);
        }

        return "index";
    }

    /**
     * Отображает страницу "О нас".
     * @return имя шаблона about
     */
    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    /**
     * Сохраняет новое платье.
     * @param dress объект платья с валидацией
     * @param bindingResult результат валидации
     * @param designerId идентификатор дизайнера
     * @param model модель для передачи данных
     * @return перенаправление на /dresses или возврат к странице index при ошибках валидации
     */
    @PostMapping("/save")
    public String saveDress(
            @Valid @ModelAttribute("dress") Dress dress,
            BindingResult bindingResult,
            @RequestParam("designerId") Long designerId,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("listDresses", service.listAll(null));
            model.addAttribute("keyword", "");
            model.addAttribute("listDesigners", designerService.findAll());
            return "index";
        }

        Designer designer = designerService.findById(designerId);
        if(designer != null) {
            dress.setDesigner(designer);
            service.save(dress);
        }
        return "redirect:/dresses";
    }

    /**
     * Обновляет существующее платье.
     * @param newDress новый объект платья с обновлёнными данными и валидацией
     * @param bindingResult результат валидации
     * @param designerId идентификатор дизайнера
     * @param model модель для передачи данных
     * @return перенаправление на /dresses или возврат к странице index при ошибках валидации
     */
    @PostMapping("/update")
    public String updateDress(
            @Valid @ModelAttribute("dress") Dress newDress,
            BindingResult bindingResult,
            @RequestParam("designerId") Long designerId,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("listDresses", service.listAll(null));
            model.addAttribute("keyword", "");
            model.addAttribute("listDesigners", designerService.findAll());
            return "index";
        }

        Dress existing = service.get(newDress.getId());
        if (existing != null) {
            Designer designer = designerService.findById(designerId);
            if (designer == null) {
                return "redirect:/dresses";
            }
            existing.setName(newDress.getName());
            existing.setStyle(newDress.getStyle());
            existing.setSize(newDress.getSize());
            existing.setColor(newDress.getColor());
            existing.setPrice(newDress.getPrice());
            existing.setArrivalDate(newDress.getArrivalDate());
            existing.setDesigner(designer);
            service.save(existing);
        }
        return "redirect:/dresses";
    }

    /**
     * Удаляет платье по идентификатору.
     * @param id идентификатор платья для удаления
     * @return перенаправление на /dresses
     */
    @PostMapping("/delete")
    public String deleteDress(@RequestParam("id") Long id) {
        service.delete(id);
        return "redirect:/dresses";
    }

    /**
     * Отображает страницу с гистограммой статистики.
     * @param model модель для передачи данных в шаблон
     * @return имя шаблона histogram
     */
    @GetMapping("/histogram")
    public String showHistogram(Model model) {
        List<Dress> listDresses = service.listAll(null);
        model.addAttribute("dressCountByDate", service.getDressCountByDate(listDresses));
        model.addAttribute("averagePrice", service.getAveragePrice(listDresses));
        model.addAttribute("topMonth", service.getMostPopularMonth(listDresses));
        return "histogram";
    }
}
