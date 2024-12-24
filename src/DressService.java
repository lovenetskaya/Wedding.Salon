/**
 * Сервисный класс для работы с платьями.
 * Предоставляет функционал для сохранения, получения, удаления и статистики по платьям.
 */
package com.example.weddingsalon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DressService {

    /**
     * Репозиторий для работы с платьями.
     */
    @Autowired
    private DressRepository repo;

    /**
     * Возвращает список всех платьев или результаты поиска по ключевому слову.
     * @param keyword ключевое слово (опционально)
     * @return список платьев
     */
    public List<Dress> listAll(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return repo.search(keyword);
        }
        return repo.findAll();
    }

    /**
     * Сохраняет новое или обновлённое платье.
     * @param dress объект платья
     * @return сохранённый объект платья
     */
    public Dress save(Dress dress) {
        return repo.save(dress);
    }

    /**
     * Возвращает платье по идентификатору.
     * @param id идентификатор платья
     * @return объект Dress или null, если не найден
     */
    public Dress get(Long id) {
        return repo.findById(id).orElse(null);
    }

    /**
     * Удаляет платье по идентификатору.
     * @param id идентификатор платья
     */
    public void delete(Long id) {
        repo.deleteById(id);
    }

    /**
     * Возвращает карту: дата поступления -> количество платьев в эту дату.
     * @param listDresses список всех платьев
     * @return отображение даты в количество поступивших платьев
     */
    public Map<String, Long> getDressCountByDate(List<Dress> listDresses) {
        return listDresses.stream()
                .filter(d -> d.getArrivalDate() != null)
                .collect(Collectors.groupingBy(
                        d -> d.getArrivalDate().toString(),
                        TreeMap::new,
                        Collectors.counting()
                ));
    }

    /**
     * Возвращает среднюю цену всех платьев.
     * @param listDresses список платьев
     * @return средняя цена или 0.0, если данных нет
     */
    public Double getAveragePrice(List<Dress> listDresses) {
        return listDresses.stream()
                .filter(d -> d.getPrice() != null)
                .mapToDouble(Dress::getPrice)
                .average()
                .orElse(0.0);
    }

    /**
     * Возвращает самый популярный месяц поступления платьев (месяц с наибольшим количеством поступлений).
     * @param listDresses список платьев
     * @return название самого популярного месяца или "Нет данных", если данных нет
     */
    public String getMostPopularMonth(List<Dress> listDresses) {
        Map<Month, Long> monthCount = listDresses.stream()
                .filter(d -> d.getArrivalDate() != null)
                .collect(Collectors.groupingBy(d -> d.getArrivalDate().getMonth(), Collectors.counting()));

        return monthCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey().name())
                .orElse("Нет данных");
    }
}
