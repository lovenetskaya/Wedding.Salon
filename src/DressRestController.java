/**
 * REST-контроллер для работы с платьями по API.
 * Предоставляет методы для получения списка, получения по ID, создания, обновления и удаления.
 */
package com.example.weddingsalon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dresses")
public class DressRestController {

    /**
     * Сервис для работы с платьями.
     */
    @Autowired
    private DressService dressService;

    /**
     * Сервис для работы с дизайнерами.
     */
    @Autowired
    private DesignerService designerService;

    /**
     * Получить все платья с возможным поиском по ключевому слову.
     * @param keyword ключевое слово (опционально)
     * @return список платьев
     */
    @GetMapping
    public ResponseEntity<List<Dress>> getAll(@RequestParam(value="keyword", required=false) String keyword){
        return ResponseEntity.ok(dressService.listAll(keyword));
    }

    /**
     * Получить платье по идентификатору.
     * @param id идентификатор платья
     * @return найденное платье или статус 404, если не найдено
     */
    @GetMapping("/{id}")
    public ResponseEntity<Dress> getOne(@PathVariable Long id){
        Dress d = dressService.get(id);
        if(d == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(d);
    }

    /**
     * Создать новое платье.
     * @param dress объект платья
     * @param designerId идентификатор дизайнера
     * @return созданное платье или статус 400 при неверном дизайнере
     */
    @PostMapping
    public ResponseEntity<Dress> create(@RequestBody Dress dress,
                                        @RequestParam Long designerId){
        Designer designer = designerService.findById(designerId);
        if (designer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        dress.setDesigner(designer);
        Dress saved = dressService.save(dress);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Обновить данные платья.
     * @param id идентификатор платья
     * @param newDress новый объект платья с обновлёнными данными
     * @param designerId идентификатор дизайнера
     * @return обновлённое платье или статус 404, если платье не найдено, или 400 при неверном дизайнере
     */
    @PutMapping("/{id}")
    public ResponseEntity<Dress> update(@PathVariable Long id,
                                        @RequestBody Dress newDress,
                                        @RequestParam Long designerId){
        Dress existing = dressService.get(id);
        if(existing == null) {
            return ResponseEntity.notFound().build();
        }
        Designer designer = designerService.findById(designerId);
        if (designer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        existing.setName(newDress.getName());
        existing.setStyle(newDress.getStyle());
        existing.setSize(newDress.getSize());
        existing.setColor(newDress.getColor());
        existing.setPrice(newDress.getPrice());
        existing.setArrivalDate(newDress.getArrivalDate());
        existing.setDesigner(designer);

        dressService.save(existing);
        return ResponseEntity.ok(existing);
    }

    /**
     * Удалить платье по идентификатору.
     * @param id идентификатор платья
     * @return статус 204 при успешном удалении или 404, если платье не найдено
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        Dress d = dressService.get(id);
        if(d == null) {
            return ResponseEntity.notFound().build();
        }
        dressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
