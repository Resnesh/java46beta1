package kz.edu.java46.library.ui;

import kz.edu.java46.library.model.Reader;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ReaderTableModel extends AbstractTableModel {
    private final List<Reader> items = new ArrayList<>();
    private final String[] columns = {"ID", "Full Name", "Contact"};

    public void setItems(java.util.List<Reader> list) {
        items.clear();
        if (list != null) items.addAll(list);
        fireTableDataChanged();
    }

    public Reader getItem(int row) { return items.get(row); }

    @Override
    public int getRowCount() { return items.size(); }

    @Override
    public int getColumnCount() { return columns.length; }

    @Override
    public String getColumnName(int column) { return columns[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Reader r = items.get(rowIndex);
        switch (columnIndex) {
            case 0: return r.getId();
            case 1: return r.getFullName();
            case 2: return r.getContact();
            default: return "";
        }
    }
}