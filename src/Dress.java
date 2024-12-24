/**
 * Класс Dress представляет сущность платья.
 * Хранит информацию о названии, стиле, размере, цвете, цене, дате поступления и дизайнере.
 */
package com.example.weddingsalon;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import jakarta.validation.constraints.Min;

@Getter
@Setter
@Entity
public class Dress {

    /**
     * Уникальный идентификатор платья.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название платья.
     */
    private String name;

    /**
     * Стиль платья.
     */
    private String style;

    /**
     * Размер платья.
     */
    private String size;

    /**
     * Цвет платья.
     */
    private String color;

    /**
     * Цена платья.
     * Валидируется на неотрицательность.
     */
    @Min(value = 0, message = "Цена не может быть отрицательной")
    private Double price;

    /**
     * Дата поступления платья.
     */
    private LocalDate arrivalDate;

    /**
     * Дизайнер, создавший платье.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id")
    private Designer designer;

    /**
     * Конструктор без аргументов.
     */
    public Dress() {
    }

    /**
     * Конструктор с параметрами.
     * @param name название платья
     * @param style стиль платья
     * @param size размер платья
     * @param color цвет платья
     * @param price цена платья
     * @param arrivalDate дата поступления
     * @param designer дизайнер платья
     */
    public Dress(String name, String style, String size, String color, Double price, LocalDate arrivalDate, Designer designer) {
        this.name = name;
        this.style = style;
        this.size = size;
        this.color = color;
        this.price = price;
        this.arrivalDate = arrivalDate;
        this.designer = designer;
    }
}
