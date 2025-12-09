package kz.edu.java46.library.ui;

import kz.edu.java46.library.model.Loan;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class LoanTableModel extends AbstractTableModel {
    private final List<Loan> items = new ArrayList<>();
    private final String[] columns = {"ID", "ReaderId", "BookId", "Issued", "Due", "Returned", "Status"};

    public void setItems(java.util.List<Loan> list) {
        items.clear();
        if (list != null) items.addAll(list);
        fireTableDataChanged();
    }

    public Loan getItem(int row) { return items.get(row); }

    @Override
    public int getRowCount() { return items.size(); }

    @Override
    public int getColumnCount() { return columns.length; }

    @Override
    public String getColumnName(int column) { return columns[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Loan l = items.get(rowIndex);
        switch (columnIndex) {
            case 0: return l.getId();
            case 1: return l.getReaderId();
            case 2: return l.getBookId();
            case 3: return l.getDateIssued();
            case 4: return l.getDateDue();
            case 5: return l.getDateReturned();
            case 6: return l.getStatus();
            default: return "";
        }
    }
}