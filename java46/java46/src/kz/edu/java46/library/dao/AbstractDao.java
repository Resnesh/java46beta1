package kz.edu.java46.library.dao;

import kz.edu.java46.library.model.AbstractEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T extends AbstractEntity> {

    protected List<T> items = new ArrayList<>();

    // Добавляет или обновляет объект.
    // Если id == null, генерируется новый ID (за счет AbstractEntity)
    public T save(T entity) {
        if (entity.getId() == null) {
            // Новый объект, ID генерируется в AbstractEntity конструкторе
            items.add(entity);
            return entity;
        } else {
            // Обновление существующего
            findById(entity.getId()).ifPresent(old -> {
                // Удаляем старый объект и добавляем обновленный
                items.remove(old);
                items.add(entity);
            });
            return entity;
        }
    }

    public Optional<T> findById(Long id) {
        return items.stream()
                .filter(item -> item.getId() != null && item.getId().equals(id))
                .findFirst();
    }

    public List<T> findAll() {
        return items;
    }

    // Метод, чтобы DataStorage мог загружать данные в этот DAO
    public void setItems(List<T> newItems) {
        this.items = newItems;
    }
}