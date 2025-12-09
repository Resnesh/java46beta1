package kz.edu.java46.library;

import kz.edu.java46.library.service.LibraryService;
import kz.edu.java46.library.model.Book;
import kz.edu.java46.library.model.Reader;
import kz.edu.java46.library.model.Loan;
import kz.edu.java46.library.model.Fine;

import java.time.LocalDate;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        LibraryService svc = new LibraryService();

        // Добавим немного данных, если таблицы пусты
        if (svc.listBooks().isEmpty()) {
            Book b1 = svc.addBook("Clean Code", "Robert C. Martin", "978-0132350884");
            Book b2 = svc.addBook("Effective Java", "Joshua Bloch", "978-0134685991");
            System.out.println("Added books: " + b1 + ", " + b2);
        }

        if (svc.listReaders().isEmpty()) {
            Reader r = svc.addReader("Иван Иванов", "ivan@example.com");
            System.out.println("Added reader: " + r);
        }

        // Выдача книги (например, 30 дней назад, срок возврата 16 дней назад)
        List<Reader> readers = svc.listReaders();
        List<Book> books = svc.listBooks();
        if (!readers.isEmpty() && !books.isEmpty()) {
            Loan loan = svc.issueLoan(readers.get(0).getId(), books.get(0).getId(), LocalDate.now().minusDays(30), LocalDate.now().minusDays(16));
            System.out.println("Issued loan: " + loan);
        }

        // Рассчитываем просрочки и сохраняем штрафы
        List<Loan> overdue = svc.findAllOverdueLoans();
        for (Loan l : overdue) {
            Fine f = svc.calculateFine(l);
            if (f.getAmount() > 0) svc.saveFine(f);
            System.out.println("Loan " + l.getId() + " fine: " + f.getAmount());
        }

        System.out.println("Fines in DB: " + svc.listFines());
        System.out.println("App finished. H2 DB file is ./data/librarydb.mv.db (or in-memory if configured).");
    }
}