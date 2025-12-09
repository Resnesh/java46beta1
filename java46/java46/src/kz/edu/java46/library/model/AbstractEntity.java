package kz.edu.java46.library.model;

import java.io.Serializable;

public abstract class AbstractEntity implements Serializable {
    private Long id;
    // Используем статическое поле для генерации уникальных ID в памяти.
    private static long ID_COUNTER = 0L;

    // Конструктор по умолчанию для Jackson и для создания новых объектов
    public AbstractEntity() {
        // Присваиваем следующий уникальный ID
        this.id = ++ID_COUNTER;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        // Важно: При загрузке данных из JSON мы должны обновить счетчик,
        // чтобы новые объекты не получили ID, которые уже есть в файле.
        if (id > ID_COUNTER) {
            ID_COUNTER = id;
        }
    }

    // *В реальных классах (Reader, Book, Loan) теперь замени UUID на Long.*
}