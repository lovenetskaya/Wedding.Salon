/**
 * Репозиторий для сущности Dress.
 * Предоставляет метод поиска платьев по ключевому слову.
 */
package com.example.weddingsalon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DressRepository extends JpaRepository<Dress, Long> {

    /**
     * Поиск платьев по ключевому слову среди названия, стиля, размера, цвета, цены, даты поступления и данных дизайнера.
     * @param searchTerm строка для поиска
     * @return список найденных платьев
     */
    @Query("SELECT d FROM Dress d JOIN d.designer ds " +
            "WHERE CONCAT(d.name, ' ', d.style, ' ', d.size, ' ', d.color, ' ', d.price, ' ', d.arrivalDate, ' ', ds.name, ' ', ds.contactInfo) " +
            "LIKE %:searchTerm%")
    List<Dress> search(@Param("searchTerm") String searchTerm);
}
