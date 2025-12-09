package kz.edu.java46.library.service;

import kz.edu.java46.library.model.Book;
import kz.edu.java46.library.model.Fine;
import kz.edu.java46.library.model.Loan;
import kz.edu.java46.library.model.Reader;

import java.util.List;

public interface DataRepository {
    // Методы для загрузки данных из файла
    List<Reader> loadReaders();
    List<Book> loadBooks();
    List<Loan> loadLoans();
    List<Fine> loadFines();

    // Метод для сохранения всех данных в файл
    void persistAllData(List<Reader> readers, List<Book> books, List<Loan> loans, List<Fine> fines);
}