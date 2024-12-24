/**
 * Класс Designer представляет сущность дизайнера свадебных платьев.
 * Хранит информацию о дизайнере, включая имя, контактную информацию и список платьев.
 */
package com.example.weddingsalon;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Designer {

    /**
     * Уникальный идентификатор дизайнера.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя дизайнера или название бренда.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Контактная информация о дизайнере.
     */
    private String contactInfo;

    /**
     * Список платьев, созданных дизайнером.
     */
    @OneToMany(mappedBy = "designer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dress> dresses = new ArrayList<>();

    /**
     * Конструктор без аргументов.
     */
    public Designer() {}

    /**
     * Конструктор с параметрами.
     * @param name имя дизайнера или бренда
     * @param contactInfo контактная информация
     */
    public Designer(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }
}
