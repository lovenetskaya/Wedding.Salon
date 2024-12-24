/**
 * Сервисный класс для управления данными о дизайнерах.
 * Осуществляет операции сохранения, поиска и удаления дизайнеров.
 */
package com.example.weddingsalon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignerService {

    /**
     * Репозиторий для работы с дизайнерами.
     */
    @Autowired
    private DesignerRepository designerRepository;

    /**
     * Возвращает список всех дизайнеров.
     * @return список дизайнеров
     */
    public List<Designer> findAll() {
        return designerRepository.findAll();
    }

    /**
     * Находит дизайнера по идентификатору.
     * @param id идентификатор дизайнера
     * @return объект Designer или null, если не найден
     */
    public Designer findById(Long id) {
        return designerRepository.findById(id).orElse(null);
    }

    /**
     * Сохраняет или обновляет данные дизайнера.
     * @param designer объект дизайнера для сохранения
     * @return сохранённый объект Designer
     */
    public Designer save(Designer designer) {
        return designerRepository.save(designer);
    }

    /**
     * Удаляет дизайнера по идентификатору.
     * @param id идентификатор дизайнера
     */
    public void delete(Long id) {
        designerRepository.deleteById(id);
    }
}
