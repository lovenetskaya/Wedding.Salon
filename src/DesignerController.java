/**
 * Контроллер для управления дизайнерами.
 * Обеспечивает методы для отображения, добавления, обновления и удаления дизайнеров.
 */
package com.example.weddingsalon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/designers")
public class DesignerController {

    /**
     * Сервисный слой для работы с дизайнерами.
     */
    @Autowired
    private DesignerService designerService;

    /**
     * Отображает страницу со списком дизайнеров.
     * @param model модель для передачи данных в шаблон
     * @return имя шаблона designers
     */
    @GetMapping
    public String showDesignersPage(Model model) {
        model.addAttribute("designer", new Designer());
        model.addAttribute("listDesigners", designerService.findAll());
        return "designers";
    }

    /**
     * Сохраняет нового дизайнера.
     * @param designer объект дизайнера для сохранения
     * @return перенаправление на страницу со списком дизайнеров
     */
    @PostMapping("/save")
    public String saveDesigner(@ModelAttribute("designer") Designer designer) {
        designerService.save(designer);
        return "redirect:/designers";
    }

    /**
     * Обновляет данные существующего дизайнера.
     * @param id идентификатор дизайнера
     * @param name новое имя дизайнера
     * @param contactInfo новая контактная информация
     * @return перенаправление на страницу со списком дизайнеров
     */
    @PostMapping("/update")
    public String updateDesigner(@RequestParam("id") Long id,
                                 @RequestParam("name") String name,
                                 @RequestParam("contactInfo") String contactInfo) {
        Designer existing = designerService.findById(id);
        if (existing != null) {
            existing.setName(name);
            existing.setContactInfo(contactInfo);
            designerService.save(existing);
        }
        return "redirect:/designers";
    }

    /**
     * Удаляет дизайнера по идентификатору.
     * @param id идентификатор дизайнера для удаления
     * @return перенаправление на страницу со списком дизайнеров
     */
    @PostMapping("/delete")
    public String deleteDesigner(@RequestParam("id") Long id) {
        designerService.delete(id);
        return "redirect:/designers";
    }
}
