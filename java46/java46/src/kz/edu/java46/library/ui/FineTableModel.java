package kz.edu.java46.library.ui;

import kz.edu.java46.library.model.Fine;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class FineTableModel extends AbstractTableModel {
    private final List<Fine> items = new ArrayList<>();
    private final String[] columns = {"ID", "LoanId", "Amount", "Paid", "Calculated"};

    public void setItems(List<Fine> list) {
        items.clear();
        if (list != null) items.addAll(list);
        fireTableDataChanged();
    }

    public Fine getItem(int row) { return items.get(row); }

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
            case 2: return Double.class;
            case 3: return Boolean.class;
            default: return String.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Fine f = items.get(rowIndex);
        switch (columnIndex) {
            case 0: return f.getId();
            case 1: return f.getLoanId();
            case 2: return f.getAmount();
            case 3: return f.isPaid();
            case 4: return f.getDateCalculated();
            default: return "";
        }
    }
}