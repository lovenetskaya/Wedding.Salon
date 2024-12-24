/**
 * Репозиторий для работы с сущностью Designer.
 * Предоставляет стандартные методы JPA для CRUD операций.
 */
package com.example.weddingsalon;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignerRepository extends JpaRepository<Designer, Long> {
}
