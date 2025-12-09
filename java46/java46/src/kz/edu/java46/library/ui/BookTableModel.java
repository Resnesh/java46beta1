package kz.edu.java46.library.ui;

import kz.edu.java46.library.model.Book;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BookTableModel extends AbstractTableModel {
    private final List<Book> items = new ArrayList<>();
    private final String[] columns = {"ID", "Title", "Author", "ISBN", "Status"};

    public void setItems(List<Book> list) {
        items.clear();
        if (list != null) items.addAll(list);
        fireTableDataChanged();
    }

    public Book getItem(int row) {
        return items.get(row);
    }

    @Override
    public int getRowCount() { return items.size(); }

    @Override
    public int getColumnCount() { return columns.length; }

    @Override
    public String getColumnName(int column) { return columns[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book b = items.get(rowIndex);
        switch (columnIndex) {
            case 0: return b.getId();
            case 1: return b.getTitle();
            case 2: return b.getAuthor();
            case 3: return b.getIsbn();
            case 4: return b.getStatus();
            default: return "";
        }
    }
}