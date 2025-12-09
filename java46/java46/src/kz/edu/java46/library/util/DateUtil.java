package kz.edu.java46.library.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    // Приватный конструктор, так как это класс-утилита
    private DateUtil() {}

    /**
     * Вычисляет количество полных дней между start и end.
     * Если end раньше или равно start, возвращает 0.
     * @param start Начальная дата (обычно плановая дата возврата)
     * @param end Конечная дата (текущая или фактическая дата возврата)
     * @return Количество дней
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        if (end.isAfter(start)) {
            return ChronoUnit.DAYS.between(start, end);
        }
        return 0;
    }
}