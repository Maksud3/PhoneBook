package com.hifeful.PhoneBook;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ContactsTableModel extends AbstractTableModel {
    private String[] columnNames = { "№", "First name", "Surname", "Last name", "Phone number", "Address" };

    private ArrayList<ArrayList<Object>> rows;
    private int deletedIndexes;

    public ContactsTableModel()
    {
        rows = new ArrayList<>();
        deletedIndexes = 0;
    }

    public int getRowCount()
    {
        return rows.size();
    }

    public int getColumnCount()
    {
        return columnNames.length;
    }

    public Object getValueAt(int r, int c)
    {
        return rows.get(r).get(c);
    }

    public String getColumnName(int c)
    {
        return columnNames[c];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        rows.get(rowIndex).set(columnIndex, aValue);
    }

    public void addRow(ArrayList<Object> row)
    {
        rows.add(row);
    }

    public void removeRow(int index)
    {
        rows.remove(index);
    }

    public int getDeletedIndexes()
    {
        return deletedIndexes;
    }

    public void setDeletedIndexes(int aDeletedIndexes)
    {
        deletedIndexes = aDeletedIndexes;
    }
}
