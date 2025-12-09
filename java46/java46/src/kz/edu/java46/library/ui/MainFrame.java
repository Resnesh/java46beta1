package kz.edu.java46.library.ui;

import kz.edu.java46.library.model.Book;
import kz.edu.java46.library.model.Fine;
import kz.edu.java46.library.model.Loan;
import kz.edu.java46.library.model.Reader;
import kz.edu.java46.library.service.FineService;
import kz.edu.java46.library.service.LibraryService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;

public class MainFrame extends JFrame {

    private final LibraryService libraryService;
    private final FineService fineService;

    private final BookTableModel bookModel = new BookTableModel();
    private final ReaderTableModel readerModel = new ReaderTableModel();
    private final LoanTableModel loanModel = new LoanTableModel();
    private final EnhancedFineTableModel fineModel;

    public MainFrame(LibraryService libraryService, FineService fineService) {
        super("Library App - Java46");
        this.libraryService = libraryService;
        this.fineService = fineService;
        this.fineModel = new EnhancedFineTableModel(libraryService);
        initUI();
        loadData();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // Books tab
        JPanel booksPanel = new JPanel(new BorderLayout());
        JTable booksTable = new JTable(bookModel);
        booksPanel.add(new JScrollPane(booksTable), BorderLayout.CENTER);
        JPanel booksButtons = new JPanel();
        JButton addBookBtn = new JButton("Add Book");
        addBookBtn.addActionListener(this::onAddBook);
        booksButtons.add(addBookBtn);
        booksPanel.add(booksButtons, BorderLayout.SOUTH);
        tabs.add("Books", booksPanel);

        // Readers tab
        JPanel readersPanel = new JPanel(new BorderLayout());
        JTable readersTable = new JTable(readerModel);
        readersPanel.add(new JScrollPane(readersTable), BorderLayout.CENTER);
        JPanel readersButtons = new JPanel();
        JButton addReaderBtn = new JButton("Add Reader");
        addReaderBtn.addActionListener(this::onAddReader);
        readersButtons.add(addReaderBtn);
        readersPanel.add(readersButtons, BorderLayout.SOUTH);
        tabs.add("Readers", readersPanel);

        // Loans tab
        JPanel loansPanel = new JPanel(new BorderLayout());
        JTable loansTable = new JTable(loanModel);
        loansPanel.add(new JScrollPane(loansTable), BorderLayout.CENTER);
        JPanel loansButtons = new JPanel();
        JButton issueLoanBtn = new JButton("Issue Loan");
        issueLoanBtn.addActionListener(e -> onIssueLoanDialog());
        JButton returnLoanBtn = new JButton("Return Loan");
        returnLoanBtn.addActionListener(e -> onReturnLoan(loansTable));
        JButton calcFinesBtn = new JButton("Calculate & Save Fines");
        calcFinesBtn.addActionListener(this::onCalculateAndSaveFines);
        loansButtons.add(issueLoanBtn);
        loansButtons.add(returnLoanBtn);
        loansButtons.add(calcFinesBtn);
        loansPanel.add(loansButtons, BorderLayout.SOUTH);
        tabs.add("Loans", loansPanel);

        // Fines tab
        JPanel finesPanel = new JPanel(new BorderLayout());
        JTable finesTable = new JTable(fineModel);
        finesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Double-click to show fine details
        finesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = finesTable.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        onShowFineDetails(row);
                    }
                }
            }
        });
        finesPanel.add(new JScrollPane(finesTable), BorderLayout.CENTER);
        JPanel finesButtons = new JPanel();
        JButton refreshFinesBtn = new JButton("Refresh");
        refreshFinesBtn.addActionListener(e -> refreshFines());
        JButton markPaidBtn = new JButton("Mark Paid");
        markPaidBtn.addActionListener(e -> onMarkFinePaid(finesTable));
        finesButtons.add(refreshFinesBtn);
        finesButtons.add(markPaidBtn);
        finesPanel.add(finesButtons, BorderLayout.SOUTH);
        tabs.add("Fines", finesPanel);

        add(tabs, BorderLayout.CENTER);
    }

    private void loadData() {
        bookModel.setItems(libraryService.listBooks());
        readerModel.setItems(libraryService.listReaders());
        loanModel.setItems(libraryService.listLoans());
        fineModel.setItems(libraryService.listFines());
    }

    private void onAddBook(ActionEvent e) {
        String title = JOptionPane.showInputDialog(this, "Title:");
        if (title == null) return;
        String author = JOptionPane.showInputDialog(this, "Author:");
        if (author == null) return;
        String isbn = JOptionPane.showInputDialog(this, "ISBN:");
        if (isbn == null) return;
        Book b = libraryService.addBook(title, author, isbn);
        bookModel.setItems(libraryService.listBooks());
        JOptionPane.showMessageDialog(this, "Added: " + b);
    }

    private void onAddReader(ActionEvent e) {
        String name = JOptionPane.showInputDialog(this, "Full name:");
        if (name == null) return;
        String contact = JOptionPane.showInputDialog(this, "Contact:");
        if (contact == null) return;
        Reader r = libraryService.addReader(name, contact);
        readerModel.setItems(libraryService.listReaders());
        JOptionPane.showMessageDialog(this, "Added: " + r);
    }

    // Issue loan via dialogs with combo boxes
    private void onIssueLoanDialog() {
        List<Reader> readers = libraryService.listReaders();
        List<Book> books = libraryService.listBooks();
        if (readers.isEmpty() || books.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Need at least one reader and one book.");
            return;
        }

        String[] readerItems = readers.stream()
                .map(r -> r.getId() + " - " + r.getFullName())
                .toArray(String[]::new);
        String selectedReader = (String) JOptionPane.showInputDialog(this, "Select reader:", "Reader",
                JOptionPane.PLAIN_MESSAGE, null, readerItems, readerItems[0]);
        if (selectedReader == null) return;
        Long rid = Long.parseLong(selectedReader.split(" - ")[0]);

        String[] bookItems = books.stream()
                .map(b -> b.getId() + " - " + b.getTitle())
                .toArray(String[]::new);
        String selectedBook = (String) JOptionPane.showInputDialog(this, "Select book:", "Book",
                JOptionPane.PLAIN_MESSAGE, null, bookItems, bookItems[0]);
        if (selectedBook == null) return;
        Long bid = Long.parseLong(selectedBook.split(" - ")[0]);

        String daysStr = JOptionPane.showInputDialog(this, "Loan period days (default 14):", "14");
        if (daysStr == null) return;
        int days;
        try {
            days = Integer.parseInt(daysStr.trim());
            if (days <= 0) days = 14;
        } catch (Exception ex) {
            days = 14;
        }

        LocalDate issued = LocalDate.now();
        LocalDate due = issued.plusDays(days);
        Loan loan = libraryService.issueLoan(rid, bid, issued, due);
        loanModel.setItems(libraryService.listLoans());
        JOptionPane.showMessageDialog(this, "Issued: " + loan);
    }

    private void onReturnLoan(JTable loansTable) {
        int row = loansTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a loan in the table first.");
            return;
        }
        Loan loan = loanModel.getItem(row);
        libraryService.returnLoan(loan.getId(), LocalDate.now());
        loanModel.setItems(libraryService.listLoans());
        JOptionPane.showMessageDialog(this, "Loan returned: " + loan.getId());
    }

    // Calculate fines and save them to repo
    private void onCalculateAndSaveFines(ActionEvent e) {
        List<Loan> overdue = libraryService.findAllOverdueLoans();
        if (overdue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No overdue loans.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Loan l : overdue) {
            Fine fine = fineService.calculateFine(l);
            boolean exists = libraryService.listFines().stream().anyMatch(f -> f.getLoanId().equals(l.getId()));
            if (!exists && fine.getAmount() > 0) {
                libraryService.saveFine(fine);
            }
            sb.append("Loan ").append(l.getId()).append(": ").append(fine.getAmount()).append("\n");
        }
        libraryService.persistAll();
        refreshFines();
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Fines calculated", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshFines() {
        fineModel.setItems(libraryService.listFines());
    }

    private void onMarkFinePaid(JTable finesTable) {
        int row = finesTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a fine in the table first.");
            return;
        }
        Fine fine = fineModel.getItem(row);
        if (fine.isPaid()) {
            JOptionPane.showMessageDialog(this, "Already paid.");
            return;
        }
        int res = JOptionPane.showConfirmDialog(this,
                "Mark fine ID " + fine.getId() + " (amount " + fine.getAmount() + ") as paid?",
                "Confirm mark paid",
                JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            libraryService.markFinePaid(fine.getId());
            refreshFines();
            libraryService.persistAll();
            JOptionPane.showMessageDialog(this, "Marked paid: fine " + fine.getId());
        }
    }

    private void onShowFineDetails(int row) {
        try {
            Fine fine = fineModel.getItem(row);
            StringBuilder sb = new StringBuilder();
            sb.append("Fine ID: ").append(fine.getId()).append("\n");
            sb.append("Loan ID: ").append(fine.getLoanId()).append("\n");
            libraryService.findLoanById(fine.getLoanId()).ifPresent(loan -> {
                sb.append("Reader ID: ").append(loan.getReaderId()).append("\n");
                sb.append("Book ID: ").append(loan.getBookId()).append("\n");
                sb.append("Issued: ").append(loan.getDateIssued()).append("\n");
                sb.append("Due: ").append(loan.getDateDue()).append("\n");
                sb.append("Returned: ").append(loan.getDateReturned()).append("\n");
            });
            sb.append("Amount: ").append(fine.getAmount()).append("\n");
            sb.append("Paid: ").append(fine.isPaid()).append("\n");
            sb.append("Calculated: ").append(fine.getDateCalculated()).append("\n");
            JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(sb.toString())), "Fine details", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error showing details: " + ex.getMessage());
        }
    }

    public void shutdown() {
        libraryService.persistAll();
    }
}