package kz.edu.java46.library.service;

import kz.edu.java46.library.model.Fine;
import kz.edu.java46.library.model.Loan;
import kz.edu.java46.library.util.DateUtil;

import java.time.LocalDate;

/**
 * Сервис, отвечающий за расчет штрафов на основе просроченных выдач.
 */
public class FineService {

    // Бизнес-правило: размер штрафа за один день просрочки
    private static final double FINE_PER_DAY = 50.0;

    /**
     * Автоматический расчет штрафа для заданной выдачи (Loan).
     * @param loan Объект выдачи (Loan).
     * @return Новый объект Fine с рассчитанной суммой.
     */
    public Fine calculateFine(Loan loan) {
        LocalDate expectedReturnDate = loan.getExpectedReturnDate();
        LocalDate targetDate;

        // 1. Определяем конечную дату для расчета:
        if (loan.getActualReturnDate() != null) {
            // Книга возвращена: используем фактическую дату возврата
            targetDate = loan.getActualReturnDate();
        } else {
            // Книга не возвращена: используем сегодняшнюю дату
            targetDate = LocalDate.now();
        }

        // 2. Считаем дни просрочки
        long overdueDays = DateUtil.daysBetween(expectedReturnDate, targetDate);

        if (overdueDays > 0) {
            double totalFine = overdueDays * FINE_PER_DAY;

            // 3. Возвращаем новый объект штрафа
            // Используем Long loan.getId()
            return new Fine(loan.getId(), totalFine, LocalDate.now());
        }

        // Если просрочки нет
        return new Fine(loan.getId(), 0.0, LocalDate.now());
    }
}