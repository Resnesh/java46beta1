package kz.edu.java46.library.service;

import kz.edu.java46.library.dao.BookDao;
import kz.edu.java46.library.dao.FineDao;
import kz.edu.java46.library.dao.LoanDao;
import kz.edu.java46.library.dao.ReaderDao;
// ... (остальные импорты)
import java.util.Optional;

// *ДОБАВЬТЕ ЭТО*
public class LibraryService {

    private final BookDao bookDao;
    private final ReaderDao readerDao;
    private final LoanDao loanDao;
    private final FineDao fineDao;
    private final DataStorage dataStorage; // Добавляем хранилище

    // Конструктор для инициализации DAOs и DataStorage
    public LibraryService(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        // Загружаем данные при создании сервиса
        dataStorage.loadAllData();

        // DAOs должны быть инициализированы после загрузки данных
        // Мы предполагаем, что DAOs получают свои данные из DataStorage,
        // но пока оставим их простой инициализацией для компиляции.
        this.bookDao = new BookDao();
        this.readerDao = new ReaderDao();
        this.loanDao = new LoanDao();
        this.fineDao = new FineDao();
    }
    // *КОНЕЦ ДОБАВЛЕННОГО КОНСТРУКТОРА*

    // ... (старые методы, которые ты предоставил) ...
    // ... (listBooks, addBook, listReaders, addReader, issueLoan, listLoans, findLoanById) ...

    public void returnLoan(Long loanId, LocalDate returned) throws Exception {
        Optional<Loan> opt = loanDao.findById(loanId);
        if (opt.isPresent()) {
            Loan l = opt.get();
            l.setDateReturned(returned);
            loanDao.save(l);
        }
    }
    public List<Loan> findAllOverdueLoans() throws Exception { return loanDao.findAllOverdueLoans(); }

    // Fine calculation (оставляем твою реализацию, она верна!)
    public Fine calculateFine(Loan loan) {
        LocalDate due = loan.getDateDue();
        LocalDate returned = loan.getDateReturned() != null ? loan.getDateReturned() : LocalDate.now();
        // Используем твою формулу: 10.0 за единицу
        long days = ChronoUnit.DAYS.between(due, returned);
        double amount = days > 0 ? days * 10.0 : 0.0;
        Fine f = new Fine(null, loan.getId(), amount, false, LocalDate.now());
        return f;
    }

    public Fine saveFine(Fine f) throws Exception { return fineDao.save(f); }
    public List<Fine> listFines() throws Exception { return fineDao.findAll(); }
    public void markFinePaid(Long fineId) throws Exception {
        Optional<Fine> opt = fineDao.findById(fineId);
        if (opt.isPresent()) {
            Fine f = opt.get();
            f.setPaid(true);
            fineDao.save(f);
        }
    }

    // *ДОБАВЛЕНИЕ НЕДОСТАЮЩЕГО МЕТОДА*
    public void persistAll() {
        dataStorage.saveAllData();
    }
    // *КОНЕЦ ДОБАВЛЕНИЯ*
}