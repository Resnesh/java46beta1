package kz.edu.java46.library.ui;

import kz.edu.java46.library.model.Book;
import kz.edu.java46.library.model.Fine;
import kz.edu.java46.library.model.Loan;
import kz.edu.java46.library.model.Reader;
import kz.edu.java46.library.service.LibraryService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnhancedFineTableModel extends AbstractTableModel {

    private final List<Fine> items = new ArrayList<>();
    private final String[] columns = {"ID", "LoanId", "Reader", "Book", "Amount", "Paid", "Calculated"};
    private final LibraryService libraryService;

    public EnhancedFineTableModel(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public void setItems(List<Fine> list) {
        items.clear();
        if (list != null) items.addAll(list);
        fireTableDataChanged();
    }

    public Fine getItem(int row) {
        return items.get(row);
    }

    @Override
    public int getRowCount() { return items.size(); }

    @Override
    public int getColumnCount() { return columns.length; }

    @Override
    public String getColumnName(int column) { return columns[column]; }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Long.class;
            case 1: return Long.class;
            case 4: return Double.class;
            case 5: return Boolean.class;
            default: return String.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Fine f = items.get(rowIndex);
        switch (columnIndex) {
            case 0: return f.getId();
            case 1: return f.getLoanId();
            case 2: return lookupReaderName(f);
            case 3: return lookupBookTitle(f);
            case 4: return f.getAmount();
            case 5: return f.isPaid();
            case 6: return f.getDateCalculated();
            default: return "";
        }
    }

    private String lookupReaderName(Fine f) {
        Optional<Loan> loanOpt = libraryService.findLoanById(f.getLoanId());
        if (!loanOpt.isPresent()) return "<no loan>";
        Loan loan = loanOpt.get();
        Optional<Reader> rOpt = libraryService.listReaders().stream().filter(r -> r.getId().equals(loan.getReaderId())).findFirst();
        return rOpt.map(Reader::getFullName).orElse("<unknown reader>");
    }

    private String lookupBookTitle(Fine f) {
        Optional<Loan> loanOpt = libraryService.findLoanById(f.getLoanId());
        if (!loanOpt.isPresent()) return "<no loan>";
        Loan loan = loanOpt.get();
        Optional<Book> bOpt = libraryService.listBooks().stream().filter(b -> b.getId().equals(loan.getBookId())).findFirst();
        return bOpt.map(Book::getTitle).orElse("<unknown book>");
    }
}